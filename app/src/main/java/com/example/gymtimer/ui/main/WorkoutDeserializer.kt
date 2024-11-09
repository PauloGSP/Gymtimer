import com.example.gymtimer.ui.main.Exercise
import com.example.gymtimer.ui.main.Rest
import com.example.gymtimer.ui.main.Workout
import com.google.gson.*
import java.lang.reflect.Type

class WorkoutDeserializer : JsonDeserializer<Workout> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Workout {
        val jsonObject = json.asJsonObject
        val name = jsonObject["name"].asString
        val exercisesJsonArray = jsonObject["exercises"].asJsonArray

        val exercises = mutableListOf<Any>()
        exercisesJsonArray.forEach { element ->
            val jsonObjectItem = element.asJsonObject
            if (jsonObjectItem.has("name")) {
                // Deserialize as Exercise
                exercises.add(context.deserialize<Exercise>(jsonObjectItem, Exercise::class.java))
            } else if (jsonObjectItem.has("duration")) {
                // Deserialize as Rest
                exercises.add(context.deserialize<Rest>(jsonObjectItem, Rest::class.java))
            } else {
                throw IllegalArgumentException("Unknown item type in exercises")
            }
        }

        return Workout(name, exercises)
    }
}
