# Pilot-Chatbot

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.5/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.5/reference/web/servlet.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.5/reference/using/devtools.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.5/reference/data/sql.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

---

## Running the Application

To run the application, follow these steps:

1. **Build the Project**:
   Use Maven to clean and build the project:
   ```bash
   mvn clean install
2. **Run the Application**:
   You can run the application using the following command:
   ```bash
   mvn spring-boot:run
   ```
3. **Access the Application**:
   Open your web browser and navigate to:
   ```
   http://localhost:8080
   ```
<header></header>

```
API Endpoints Overview
    Chat with the AI
    - POST /api/chat
        - Request Body: 
            { 
                "message": "Your message here"
                "model": "openai/gpt-3.5-turbo" // Optional
            }
        - Response: 
            { 
                "response": "AI's response here" 
            }

Reset Chat History
    POST /api/chat/reset
        Response: 200 OK
```
