#!/bin/bash

echo "Testing Rate Limiter on CARDS endpoint"
echo "========================================"
echo ""
echo "Rate Limiter Config: 1 request/sec, Burst: 1"
echo ""

# Test 1: Rapid requests with same user (should fail after 1st)
echo "TEST 1: Rapid requests with user 'alice' (should PASS 1st, FAIL 2nd-5th):"
echo "Expected: 1st request passes, rest get 429 Too Many Requests"
echo "---"
for i in {1..5}
do
   echo "Request $i:"
   HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -H "user: alice" http://localhost:8072/eazybank/cards/api/fetch?mobileNumber=9345432123)
   echo "HTTP Status: $HTTP_CODE"
   if [ "$HTTP_CODE" = "404" ]; then
      echo "✓ PASS (Card not found, but request passed rate limiter)"
   elif [ "$HTTP_CODE" = "429" ]; then
      echo "✓ FAIL (Rate limited - Too Many Requests)"
   else
      echo "? Status: $HTTP_CODE"
   fi
   echo ""
   sleep 0.2
done

echo ""
echo "========================================"
echo "TEST 2: Requests with proper spacing (1 sec apart, all should PASS):"
echo "Expected: All requests pass with 1 second spacing"
echo "---"
for i in {1..3}
do
   echo "Request $i:"
   HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -H "user: bob" http://localhost:8072/eazybank/cards/api/fetch?mobileNumber=9345432123)
   echo "HTTP Status: $HTTP_CODE"
   if [ "$HTTP_CODE" = "404" ]; then
      echo "✓ PASS (Card not found, but request passed rate limiter)"
   elif [ "$HTTP_CODE" = "429" ]; then
      echo "✓ FAIL (Rate limited - Too Many Requests)"
   else
      echo "? Status: $HTTP_CODE"
   fi
   echo ""
   sleep 1
done

echo ""
echo "========================================"
echo "TEST 3: Different users should have separate rate limits:"
echo "Expected: Each user gets their own 1 request/sec bucket"
echo "---"
echo "Request 1 (user: charlie):"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -H "user: charlie" http://localhost:8072/eazybank/cards/api/fetch?mobileNumber=9345432123)
echo "HTTP Status: $HTTP_CODE (Expected: 404 or 429)"
echo ""

echo "Request 2 (user: diana):"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -H "user: diana" http://localhost:8072/eazybank/cards/api/fetch?mobileNumber=9345432123)
echo "HTTP Status: $HTTP_CODE (Expected: 404 or 429)"
echo ""

echo "Request 3 (user: charlie again - should fail, same user):"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -H "user: charlie" http://localhost:8072/eazybank/cards/api/fetch?mobileNumber=9345432123)
echo "HTTP Status: $HTTP_CODE (Expected: 429 - rate limited)"
echo ""

echo ""
echo "========================================"
echo "TEST 4: Anonymous requests (no user header):"
echo "Expected: All rapid requests from anonymous user get rate limited"
echo "---"
for i in {1..3}
do
   echo "Request $i (no user header):"
   HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8072/eazybank/cards/api/fetch?mobileNumber=9345432123)
   echo "HTTP Status: $HTTP_CODE"
   if [ "$HTTP_CODE" = "404" ]; then
      echo "✓ PASS (Card not found, request passed rate limiter)"
   elif [ "$HTTP_CODE" = "429" ]; then
      echo "✓ FAIL (Rate limited - Too Many Requests)"
   else
      echo "? Status: $HTTP_CODE"
   fi
   echo ""
   sleep 0.2
done

echo ""
echo "========================================"
echo "TEST COMPLETE"
echo "If you see 429 errors on rapid requests, rate limiter is working!"
