package com.futoria.core.serializer;

import com.futoria.core.model.UserData;
import com.futoria.core.model.university.Group;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class GroupSerializer implements JsonSerializer<Group> {
    private UserDataSerializer userDataSerializer;
    private DepartmentSerializer departmentSerializer;

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
    public JsonElement serialize(Group src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonArray userDataJsonArray = new JsonArray();

        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("subGroup", src.getSubGroup());

        try {
            for (UserData userData : src.getUsersData()) {
                userDataJsonArray.add(userDataSerializer.serialize(userData, typeOfSrc, context));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jsonObject.add("usersData", userDataJsonArray);
        }

        try {
            jsonObject.add("department", departmentSerializer.serialize(src.getDepartment(), typeOfSrc, context));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jsonObject.add("department", new JsonObject());
        }

        return jsonObject;
    }

    @Autowired
    public void setUserDataSerializer(UserDataSerializer userDataSerializer) {
        this.userDataSerializer = userDataSerializer;
    }

    @Autowired
    public void setDepartmentSerializer(DepartmentSerializer departmentSerializer) {
        this.departmentSerializer = departmentSerializer;
    }
}
