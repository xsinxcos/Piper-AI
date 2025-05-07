# 使用 Maven 构建阶段
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY piper-ai-api ./piper-ai-api
COPY piper-ai-app ./piper-ai-app
COPY piper-ai-domain ./piper-ai-domain
COPY piper-ai-engine ./piper-ai-engine
COPY piper-ai-infrastructure ./piper-ai-infrastructure
COPY piper-ai-trigger ./piper-ai-trigger
COPY piper-ai-types ./piper-ai-types

# 使用 Maven 构建项目
#RUN mvn -f pom.xml clean package -Dmaven.test.skip=true

# 运行阶段
FROM ibm-semeru-runtimes:open-17-jre
WORKDIR /app
COPY --from=build /app/piper-ai-app/target/*.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"] 