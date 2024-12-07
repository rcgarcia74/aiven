# Building IoT Solution For Farm Monitoring using Aiven Services

In farming there are at least two types of data points that are critical to growth and health of crops - atmospheric and soil conditions. 
Humidity, temperature and soil moisture levels needs to be monitored to determine if crops needs more water or not. This project provides 
the code to generate sample IoT sensor data that represents atmospheric and soil conditions. See the sample dataset below in JSON.

```json
{
    "deviceId": "42dbc2e7-f3bc-4edf-8e39-6504da1dc792",
    "temperature": 98.16,
    "humidity": 64.71,
    "soilType": "SANDY",
    "moisture_level": 12.74,
    "add_water": "false",
    "recordDate": "2024-12-06 01:38:01"
}
```
It's a simple set of data but enough to provide an idea when crops needs to be watered or not. The add_water field is automatically calculated
based on some thresholds. For instance, water is needed when temperature is less than 64°F along with the set of soil moisture conditions below.

Table.1 Soil Moisture Level
| Soil Type  |  High  | Medium  | Low  |
| ---------  | :-----:| :------:| ---: |
| Clay       | 80-100 | 60-80   | <60  |
| Loamy      | 88-100 | 70-88   | <70  |
| Sandy      | 90-100 | 80-90   | <80  |  

The following tutorial shows you how to ingest the IoT data into Aiven, the unified platform to stream, store and serve data on any cloud.
You are free to use upto $300 of credits which is more than enough to test out the exercise below. 

Here's the use case that we will solve. 

1. Emit IoT event into Aiven for Apache Kafka
2. Integrate Aiven for Apache Flink & build a Flink application that filters the event using the addWater boolean attribute. Since addWater is a boolean attribute, this implies we need two new Kafka topics to store the respective events
3. Integrate Aiven for Kafka -> Aiven for Metrics -> Aiven for Grafana to gather metrics from Kafka service

This will be a complete solution to push the events into Kafka with monitoring in realtime. 

### Emit IoT event into Aiven for Apache Kafka
#### Prerequisites
You should have created your Aiven account [here](https://console.aiven.io/signup). Take note of the following information. For authentication, in this tutorial, we will use SASL.
* SASL Username
* SASL Password
* TRUSTSTORE Password
* TRUSTSTORE Location
* Bootstrap Servers

1. After creating your account, go to your Aiven console and click **Create Service** ![Aiven Create Service](aiven/images/Create_Service.png)
2. Choose **Apache Kafka** ![Select Apache Kafka](aiven/images/Select_Apache_Kafka.png)
3. Fill in the form. You can leave everything the same except for the service plan. Choose **Startup** for the service plan as it will not eat up your credits faster. Then name it **kafka-sensors** and create your service. ![Create Apache Kafka Service](aiven/images/Create_Apache_Kafka_Service.png)
4. You will be taken to the **Services** page with your Kafka service being built. You should see something similar below ![Services](aiven/images/Services.png)
5. This will take you to the **Configure your client** page ![Configure your client](aiven/images/Enable SASL.png). Click **Enable SASL**.
6. Aiven will generate the details for ther service URL and other important details ![Kafka Connection Details](aiven/images/Kafka_Client_Connection_Details.png)
7. Go to the **com.aiven.kafka.Producer** code and update the missing values for the properties mentioned above. Once you have them all populated, execute this code. 
Messages are created every second and pushed to the Kafka topic created earlier.
Go to **Topics** then create and name your **topic** and finish setup. Use the **farm_sensor** topic name.  ![Kafka Topics](aiven/images/Topics.png)

    Make sure you have 3 topics configured.
    * farm_sensor - topic that stores raw events
    * dry_soil - topic that stores transformed events that indicates crops need water
    * wet_soil - topic that stores transformed events that indicates crops doesn't need water

8. **farm_sensor** topic should have events by now. Click on **Topic message** ![Topic messages](aiven/images/Topic_Messages.png). 
Change the **FORMAT** filter to json and click on **Fetch messages**. You should see events. ![Fetch Messages](aiven/images/Fetch_Messages.png)

This completes the Kafka setup.

### Integrate Aiven for Apache Flink
Flink will be used to transform the events from farm_sensor topic then route them based on the value of **add_water** field which is a boolean. At this point, you should be familiar in creating services in Aiven. 
1. Create Apache Flink service. Choose the same cloud, region as your Kafka service. Name your service.
2. Once its created, click on Applications on the left panel then click **Create Application** ![Flink Applications](aiven/images/Flink_Create_Application.png). 
3. Click on **Create first version** ![Create first version](aiven/images/Application_Create_First_Version.png)
4. Before you add your source table, make sure that you have integrated Kafka with your Flink service. Click on **Manage integrations** and choose your kafka service and click **Integrate** ![Manage integrations](aiven/images/Flink_Kafka_Service_Integration.png) Add your source table ![Add Source Table](aiven/images/Applications_Add_First_Source_Table.png)
5. Choose your Kafka service and enter the SQL create statement ![New Source Table](aiven/images/Application_Add_New_Source_Table.png). For this table, use the [create-table-for-dry-soil.sql](aiven/flink/create-table-for-farm-sensor.sql) script. Click **Add Tabke**.
6. Click **Next** to create Sink tables ![New Sink Table](aiven/images/Application_Sink_Table.png).
7. Click **Add your first sink table** ![New Sink Table](aiven/images/Add_Your_First_Sink_Table.png).
8. Choose your Kafka service and copy/paste the sql statement from [create-table-for-dry-soil.sql](aiven/flink/create-table-for-dry-soil.sql) script. Add new table.
9. Add one more sink table using [create-table-for-wet-soil](aiven/flink/create-table-for-wet-soil.sql)
10. You should see two sink tables named dry_soil & wet_soil ![Dry & Wet Soil](aiven/images/Two_Sink_Tables.png) Click **Next**.
11. This is where you can do your transformation/filtering of events from **farm_sensor** topic. Copy/paste the statement from [farm_sensor_transform.sql](aiven/flink/farm_sensor_transform.sql) and add it to the create statement page. It does two things - transform **recordDate** field into a TIMESTAMP format so Flink can deserialize this column and filter the events based on add_water value. Click **Save and deploy later**.
12. Your Flink job is not deployed yet. You will see something similar below. It shows the data pipeline created earlier. To deploy your pipeline, click **Create deployment** ![Create Deployment](aiven/images/Create_Deployment.png)
13. Click **Deploy without a savepoint**. Once your pipeline is running, you should be able to see events in dry_soil and wet_soil topics.
14. Go to your Flink service Overview page and click on Flink Web UI URL. You will see the current job running here ![Flink Job Status](aiven/images/Flink_Job_Status.png)

This concludes the integration between Apache Kafka and Apache Flink.

### Integrate Aiven for Apache Kafka, Aiven for Metrics and Aiven for Grafana for Observability and Monitoring
At this point, we are ready to build the observability and monitoring piece of our IoT solution. We will use Aiven for Metrics to capture Kafka metrics and display them in Aiven for Grafana. 
1. Create your Aiven for Metrics and name it thanos-farm-iot. Aiven for Metrics uses Thanos to capture metrics from many systems.
2. Create your Aiven for Grafana service and name it grafana-farm-iot.
3. Go to your Aiven for Metrics page anf click on **Go to integrations** ![Thanos Integrations](aiven/images/Thanos_Integrations.png)
4. Click on **Receive Metrics** service then choose **Existing service**. From the **Service name** drop-down list, pick kafka-sensors ![Thanos Kafka Enable Integration](aiven/images/Thanos_Kafka_Sensor_Integration.png)
5.  Click **Enable**. Kafka will now be receiving service metrics from Kafka.
6.  Go to integrations again and add **Grafana Metrics Dashboard**.  ![Thanos Grafana Enable](aiven/images/Thanos_Grafana_Metrics.png)
7.  Choose **Existing service** and pick from the drop-down list **grafa-farm-iot** then click Enable
8.  Aiven for Metrics is now collecting Kafka metrics and sending it over to Aiven for Grafana dashboard.
9.  Go to Aiven for Metrics page and click on Web UI link ![Thanos UI](aiven/images/Thanos_UI.png).
10.  Type in the query field kafka. This should pull up all of the Kafka metrics. When you choose any of those metrics and execute your PromQL query, the results will show counts for that metric ![Thanos Query](aiven/images/Thanos_Query.png).
11.  Go to Aiven for Grafana service page and click on the **Service URI** link. Click on **Dashboards** ![Grafana Dashboards](aiven/images/Grafana_Dashboards.png)
12.   You should see the page below showing the kafka-sensors dashboard ![Kafka Dashboard](aiven/images/Grafana_Dasboards_Kafka.png)
13.   Click on the *kafka-sensors* dashboard row. You should see something similar below ![Grafana Kafka Dashboard Details](aiven/images/Grafana_Kafka_Dashboard_Details.png)

This concludes the observability and monitoring setup for the Kafka service. 

We now have all the pieces to capture IoT events from farms through Kafka and apply Flink job transformationT to route events to new topics by soilType field. We also have the proper monitoring in place to ensure that Kafka services are in good health. The credits burned for building this solution is <$20. 
