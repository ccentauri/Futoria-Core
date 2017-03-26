package com.futoria.core.serializer;

import com.futoria.core.model.UserData;
import com.futoria.core.model.university.Faculty;
import com.futoria.core.model.university.University;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class UniversitySerializer implements JsonSerializer<University> {
    private FacultySerializer facultySerializer;

    /**
     * Gson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     * <p>
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
     * non-trivial field of the {@code src} object. However, you should never invoke it on the
     * {@code src} object itself since that will cause an infinite loop (Gson will call your
     * call-back method again).</p>
     *
     * @param src       the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context
     * @return a JsonElement corresponding to the specified object.
     */
    @Override
    public JsonElement serialize(University src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonArray faculties = new JsonArray();

        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("shortName", src.getShortName());
        jsonObject.addProperty("longName", src.getLongName());

        try {
            for (Faculty faculty : src.getFaculties()) {
                faculties.add(facultySerializer.serialize(faculty, typeOfSrc, context));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jsonObject.add("faculties", faculties);
        }

        return jsonObject;
    }

    @Autowired
    public void setFacultySerializer(FacultySerializer facultySerializer) {
        this.facultySerializer = facultySerializer;
    }
}
