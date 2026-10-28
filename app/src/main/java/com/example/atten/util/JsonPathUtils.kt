package com.example.atten.util

object JsonPathUtils {
    /**
     * Navigate through a JSON map using a dot-separated path
     * Example: "response.status" will get map["response"]?.get("status")
     */
    fun getValueByPath(json: Map<String, Any>?, path: String): Any? {
        if (json == null || path.isEmpty()) return null
        
        val parts = path.split(".")
        var current: Any? = json
        
        for (part in parts) {
            current = when (current) {
                is Map<*, *> -> (current as Map<String, Any>).get(part)
                else -> return null
            }
        }
        
        return current
    }
    
    /**
     * Check if a value matches criteria
     */
    fun matchesCriteria(value: Any?, criteriaValue: String): Boolean {
        return when (value) {
            is String -> value.contains(criteriaValue)
            is Number -> value.toString() == criteriaValue
            is Boolean -> value.toString() == criteriaValue
            null -> false
            else -> value.toString().contains(criteriaValue)
        }
    }
    
    /**
     * Check if response contains expected criteria for success/failure
     */
    fun checkResponseCriteria(
        responseBody: Map<String, Any>?,
        jsonPath: String,
        expectedValue: String
    ): Boolean {
        if (jsonPath.isEmpty() || responseBody == null) return false
        
        val value = getValueByPath(responseBody, jsonPath)
        return matchesCriteria(value, expectedValue)
    }
}
