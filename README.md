# heys-api-server

## Stack

---
### Basic
* Kotlin
* Spring Boot 2.7.4
* Gradle Kotlin DSL
---
### Database & CI/CD
* Postgresql 14
* Jenkins
* Oracle Cloud
---

## Local Run Requirement
### Install
* JAVA 17 JDK
* Postgresql
### Run
* Prepare
  * brew services start postgresql (DB)
  * JAVA_HOME=<JAVA 17 JDK PATH> (System variable)

* Command
  * ``sh scripts/run-local.sh``
---

## Deploy
### Dev
* ``git switch main && git push`` webhook send to Jenkins