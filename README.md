# Getting Started

### Get your account and service set up

Contact office@entarc.eu to get an account for data.entarc.eu.

Once you have received your details, please go to https://data.entarc.eu/pages/services/list
and create your service. 

## Consuming the Kafka Interface

Please tell the service shortcode back to office@entarc.eu
to get a password for the Kafa interface.

_NOTE:_ This is of course going to be automated in future versions of data.entarc.eu.

_IMPORTANT:_ In order to consume the Kafka interface, ensure that the Data Need's "Send to Client Kafka" toggle is activated.

### Generate a client truststore

In order to be able to connect with data.entarc.eu in its current configuration, please first create a truststore:

For this, please use JDK standard keytool functionality.

```
keytool -keystore client.truststore.jks -storepass 'YOUR_PASSWORD'  -alias CARoot -storetype pkcs12 -import -file cacert.pem
```

Enter all needed details and set a password.

please go to /src/main/java/resources/application.properties and adapt the needed properties:

```
spring.application.name=data.entarc.eu - Java Samples

dataservices.kafka.truststore.file=client.truststore.jks
dataservices.kafka.truststore.password=PASSWORD_YOU_HAVE_SET
dataservices.kafka.username=YOUR_SERVICE_SHORTCODE
dataservices.kafka.password=PASSWORD_RECEIVED_FROM_ENTARCEU
dataservices.kafka.bootstrapurls=data.entarc.eu:9097
dataservices.kafka.groupid=UNIQUE_BUT_AS_YOU_WISH
```

## Consuming the REST API

_IMPORTANT:_ Please note that you have to activate the "Energy Data Vault" toggle for each Data Need whose traffic
is meant to be made available through the REST API. 

Please note that the service provided by the 
REST API is actually out of scope of original Project EDDIE, but brought to you by entarc.eu because
of demands expressed. We would be more then happy about your feedback and requests for features
and/or improvements.