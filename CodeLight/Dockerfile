FROM openjdk:11
################################
# Install Terraform
################################

# Download terraform for linux
RUN wget https://releases.hashicorp.com/terraform/1.1.5/terraform_1.1.5_linux_amd64.zip

# Unzip
RUN unzip terraform_1.1.5_linux_amd64.zip

# Move to local bin
RUN mv terraform /usr/local/bin/

#RUN terraform init
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY . .

RUN rm -rf terraform_node/.terraform.lock.hcl

RUN cd terraform_node
RUN terraform init

CMD ["./mvnw", "-Dit.test=features.massbit_route.Community_Nodes", "-Dskip-test=true", "-Denvironment=dev", "clean", "verify"]
