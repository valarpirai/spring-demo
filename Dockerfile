FROM amazoncorretto:11-alpine3.19
LABEL maintainer="sriram.raghavan@superops.ai"

ENV FILEBEAT_VERSION=8.7.0

RUN java -version

RUN wget -O filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz
RUN tar xzvf filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz
RUN rm -rf filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz
RUN mv filebeat-${FILEBEAT_VERSION}-linux-x86_64 /opt/filebeat

CMD ["/opt/filebeat/filebeat", "-e"]
