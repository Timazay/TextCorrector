# Text Polishing Service

A service for automatically checking and processing text using the Yandex Speller API. 
Allows you to create text checking tasks and track their progress.

### Local Launch
In the root directory, open the console and run command:
`docker compose up -d`

The application runs on port 8080.

### Swagger
http://localhost:8080/text-polish/swagger-ui.html

### Create a text processing task

Creates a new task for checking and processing text.

**Endpoint:** `POST /api/v1/text-polish-tasks`

**Request Body:**
```json
{
  "text": "Hallo world!",        
  "language": "EN"
}
```
**Constraints:**
- Parameter `language` can be either RU or EN in any case and cannot be null.
- Parameter `text` cannot contain only special characters and numbers, must be more than three characters long, and cannot be null.

**Response 200**
```json
{
  "taskId": "550e8400-e29b-41d4-a716-446655440000"
}
```
**Response 400**
```json
{
  "message": "Language cannot be null",
  "errorCode": "400",
  "timestamp": "2026-02-18T14:51:52.7853999",
  "path": "http://localhost:8080/api/v1/text-polish-tasks"
}
```

### Getting text processing results

Gets processing result of a task by its ID.

**Endpoint:** `GET /api/v1/text-polish-tasks/{taskId}`

**Constraints:**
- Parameter `taskId` must be UUID

**Response 200**

Successful processing:
```json
{
  "text": "Hello world!",
  "status": "FINISHED"
}
```
Text in processing:
```json
{
  "status": "PROCESSING"
}
```
Text could not be processed:
```json
{
  "status": "FAILED",
  "errorDescription": "processing limit exceeded"
}
```

**Response 400**
```json
{
  "message": "Method parameter 'taskId': Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: 32w34432452345234545rgtfre4tfge4r",
  "errorCode": "400",
  "timestamp": "2026-02-18T15:08:47.0176812",
  "path": "http://localhost:8080/api/v1/text-polish-tasks/32w34432452345234545rgtfre4tfge4r"
}
```

**Response 404**
```json
{
  "message": "Task with id: 3c52b478-590a-4665-b64f-49cc63caba18 not found",
  "errorCode": "404",
  "timestamp": "2026-02-18T15:09:48.4383468",
  "path": "http://localhost:8080/api/v1/text-polish-tasks/3c52b478-590a-4665-b64f-49cc63caba18"
}
```