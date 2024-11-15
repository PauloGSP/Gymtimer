package com.example.gymtimer.ui.main

import com.google.gson.*
import java.lang.reflect.Type

class WorkoutDeserializer : JsonDeserializer<Workout> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Workout {
        val jsonObject = json.asJsonObject

        // Safely getting the "name" field with a default value if missing
        val name = if (jsonObject.has("name") && !jsonObject["name"].isJsonNull) {
            jsonObject["name"].asString
        } else {
            throw JsonParseException("Workout name is missing or null")
        }

        // Safely getting the "exercises" array
        val exercisesJsonArray = if (jsonObject.has("exercises") && jsonObject["exercises"].isJsonArray) {
            jsonObject["exercises"].asJsonArray
        } else {
            throw JsonParseException("Exercises array is missing or not a JSON array")
        }

        val exercises = mutableListOf<Any>()
        exercisesJsonArray.forEach { element ->
            val jsonObjectItem = element.asJsonObject
            try {
                if (jsonObjectItem.has("name") && !jsonObjectItem["name"].isJsonNull) {
                    // Deserialize as Exercise
                    exercises.add(context.deserialize<Exercise>(jsonObjectItem, Exercise::class.java))
                } else if (jsonObjectItem.has("duration") && !jsonObjectItem["duration"].isJsonNull) {
                    // Deserialize as Rest
                    exercises.add(context.deserialize<Rest>(jsonObjectItem, Rest::class.java))
                } else {
                    throw IllegalArgumentException("Unknown item type in exercises")
                }
            } catch (e: Exception) {
                throw JsonParseException("Error deserializing item in exercises: ${e.message}", e)
            }
        }

        return Workout(name, exercises)
    }
}
