/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author prabhu.prabhakaran
 */
public class AccessPermissionTypeAdapter implements JsonSerializer<AccessPermission>, JsonDeserializer<AccessPermission> {

    @Override
    public synchronized JsonElement serialize(AccessPermission permission, Type type, JsonSerializationContext jsonSerializationContext) {
	return new JsonPrimitive(permission.code);
    }

    @Override
    public synchronized AccessPermission deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
	return AccessPermission.fromCode(jsonElement.getAsString());
    }
}
