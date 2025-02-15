package me.pieking1215.invmove;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;

public class GsonHelperFix {
    public static boolean isBooleanValue(JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean();
    }

    public static boolean isObjectNode(JsonObject jsonObject, String string) {
        return GsonHelper.isValidNode(jsonObject, string) && jsonObject.get(string).isJsonObject();
    }
}
