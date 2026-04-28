#!/bin/bash
# Manual API Testing Script for Tutorial CRUD API
# This script demonstrates all API endpoints

echo "==================================="
echo "Tutorial CRUD API - Manual Testing"
echo "==================================="
echo ""

BASE_URL="http://localhost:8080/api"

echo "1. Testing CREATE Tutorial (POST /api/tutorials)"
echo "---------------------------------------------------"
RESPONSE=$(curl -s -X POST "${BASE_URL}/tutorials" \
  -H "Content-Type: application/json" \
  -d '{"title":"FastAPI Basics","description":"Learn FastAPI framework","published":false}')
echo "Response: $RESPONSE"
TUTORIAL_ID=$(echo $RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*')
echo "Created Tutorial ID: $TUTORIAL_ID"
echo ""

echo "2. Testing GET All Tutorials (GET /api/tutorials)"
echo "---------------------------------------------------"
curl -s "${BASE_URL}/tutorials" | python3 -m json.tool
echo ""

echo "3. Testing GET Tutorial by ID (GET /api/tutorials/$TUTORIAL_ID)"
echo "---------------------------------------------------"
curl -s "${BASE_URL}/tutorials/${TUTORIAL_ID}" | python3 -m json.tool
echo ""

echo "4. Testing CREATE Another Tutorial"
echo "---------------------------------------------------"
curl -s -X POST "${BASE_URL}/tutorials" \
  -H "Content-Type: application/json" \
  -d '{"title":"Python Advanced","description":"Advanced Python concepts","published":true}' | python3 -m json.tool
echo ""

echo "5. Testing SEARCH by Title (GET /api/tutorials?title=python)"
echo "---------------------------------------------------"
curl -s "${BASE_URL}/tutorials?title=python" | python3 -m json.tool
echo ""

echo "6. Testing GET Published Tutorials (GET /api/tutorials/published)"
echo "---------------------------------------------------"
curl -s "${BASE_URL}/tutorials/published" | python3 -m json.tool
echo ""

echo "7. Testing UPDATE Tutorial (PUT /api/tutorials/$TUTORIAL_ID)"
echo "---------------------------------------------------"
curl -s -X PUT "${BASE_URL}/tutorials/${TUTORIAL_ID}" \
  -H "Content-Type: application/json" \
  -d '{"title":"FastAPI Advanced","description":"Advanced FastAPI techniques","published":true}' | python3 -m json.tool
echo ""

echo "8. Testing GET Updated Tutorial"
echo "---------------------------------------------------"
curl -s "${BASE_URL}/tutorials/${TUTORIAL_ID}" | python3 -m json.tool
echo ""

echo "9. Testing DELETE Tutorial (DELETE /api/tutorials/$TUTORIAL_ID)"
echo "---------------------------------------------------"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "${BASE_URL}/tutorials/${TUTORIAL_ID}")
echo "HTTP Status Code: $HTTP_CODE"
if [ "$HTTP_CODE" = "204" ]; then
    echo "✅ Tutorial deleted successfully"
else
    echo "❌ Failed to delete tutorial"
fi
echo ""

echo "10. Testing GET Deleted Tutorial (should return 404)"
echo "---------------------------------------------------"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}/tutorials/${TUTORIAL_ID}")
echo "HTTP Status Code: $HTTP_CODE"
if [ "$HTTP_CODE" = "404" ]; then
    echo "✅ Tutorial not found (as expected)"
else
    echo "❌ Tutorial still exists (unexpected)"
fi
echo ""

echo "11. Testing DELETE All Tutorials (DELETE /api/tutorials)"
echo "---------------------------------------------------"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "${BASE_URL}/tutorials")
echo "HTTP Status Code: $HTTP_CODE"
if [ "$HTTP_CODE" = "204" ]; then
    echo "✅ All tutorials deleted successfully"
else
    echo "❌ Failed to delete all tutorials"
fi
echo ""

echo "12. Testing GET All Tutorials (should be empty)"
echo "---------------------------------------------------"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}/tutorials")
echo "HTTP Status Code: $HTTP_CODE"
if [ "$HTTP_CODE" = "204" ]; then
    echo "✅ No content (as expected)"
else
    echo "Response:"
    curl -s "${BASE_URL}/tutorials" | python3 -m json.tool
fi
echo ""

echo "==================================="
echo "Testing Complete!"
echo "==================================="
echo ""
echo "📚 Interactive API Documentation:"
echo "   Swagger UI: http://localhost:8080/docs"
echo "   ReDoc:      http://localhost:8080/redoc"
echo ""
