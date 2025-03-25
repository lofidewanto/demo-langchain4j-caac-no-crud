# demo-langchain4j-caac-no-crud
Demo Comparison of CaaC (Chat as Code) Business Logics vs. CRUD (Create Read Update Delete) Business Logics.

# Install and run Ollama first
https://ollama.com

# Run the application
```
mvn spring-boot:run -Dspring-boot.run.arguments=chat-id
```

# Run the database user interface
For the CRUD operations, we are using the H2 in-memory database. If you want to take a look at the content of the database, just check out this simple database console.

H2 Console: http://localhost:8080/caac/h2-console

# The Story
What kind of company this is and what kind of support this company offers are described in these two text files.

Company: DieSoon, household appliances company, established in 2001 

1. Mila: our lovely customer support agent 
src/main/resources/customer-agent.txt

2. DieSoon Knowledge Management
src/main/resources/company-knowledge.txt

# Chat and conversation with the CaaC (Chat as Code) Business Logics
- Print all data of following customer: john@gmail.com 
- I'm new here and please create a new customer data for me.
  My email is hello@gmail.com.
  My name is John Doe.
  My address is 1234 Main Street, New York, NY 10001.
  I'm 30 years old.
