FROM eclipse-temurin
# Port to expose for web console
# EXPOSE 9081
# Port to expose for tcp connections
EXPOSE 9082

COPY h2-2.1.210.jar h2.jar
CMD  java -cp h2.jar org.h2.tools.Server -web -webDaemon -webAllowOthers -webPort 9081 -tcp -tcpAllowOthers -tcpPort 9082 -ifNotExists
#FROM adoptopenjdk:11-jre-hotspot

#ENV H2_ZIP http://www.h2database.com/h2-2019-03-13.zip
#ENV H2_PORT 9082
#ENV H2_CONSOLE_PORT 9082
#ENV H2_DATA /var/lib/h2

#RUN apt-get update && apt-get upgrade -y && apt-get install -y unzip && \
#    curl ${H2_ZIP} -o h2.zip && unzip -q h2.zip -d /opt/ && rm h2.zip && \
#    mkdir ${H2_DATA}

#EXPOSE ${H2_PORT} ${H2_CONSOLE_PORT}

#WORKDIR ${H2_DATA}

#CMD java -cp /opt/h2/bin/h2*.jar org.h2.tools.Server \
#    -baseDir ${H2_DATA} -ifNotExists \
#    -tcp -tcpAllowOthers -tcpPort ${H2_PORT} \
#    -web -webAllowOthers -webPort ${H2_CONSOLE_PORT}