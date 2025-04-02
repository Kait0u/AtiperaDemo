# Atipera Recruitment Demo
This is my take on the assignment, assigned by Atipera for recruitment purposes. The task was as follows: create a REST API that uses GitHub's API to get all of a provided user's non-fork repositories, along with their respective branches and their last commit SHAs.

## Request
```http request
GET http://localhost:8080/api/{github-username}
```

## Response
Upon successfully assembling the required information, an example response is as follows:
```json
{
  "username": "Kait0u",
  "repos": [
    {
      "name": "CellularAutomata",
      "branches": [
        {
          "name": "master",
          "sha": "770e81da62c022c5ddabac154e4d3847c58ad96d"
        }
      ]
    },
    {
      "name": "Clone2048",
      "branches": [
        {
          "name": "master",
          "sha": "6f6b8a8d2b4394febfad1e4f66bfef9fae61fea9"
        }
      ]
    }
  ]
}
```

Or, upon a 404 error:

```json
{
  "status": 404,
  "message": "Not Found"
}
```

