package com.hanielcota.kits.serializer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemSerializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemSerializer.class);

    public static String serializeItems(List<ItemStack> items) {
        Objects.requireNonNull(items, "A lista de itens não pode ser nula");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream bukkitOutput = new BukkitObjectOutputStream(outputStream)) {
            bukkitOutput.writeObject(items);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new ItemSerializationException("Erro ao serializar " + items.size() + " itens: " + e.getMessage(), e);
        }
    }

    public static List<ItemStack> deserializeItems(String serializedItems) {
        if (serializedItems == null || serializedItems.isEmpty()) {
            LOGGER.debug("String serializada nula ou vazia, retornando lista vazia");
            return new ArrayList<>();
        }

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(serializedItems));
             BukkitObjectInputStream bukkitInput = new BukkitObjectInputStream(inputStream)) {
            Object deserializedObject = bukkitInput.readObject();

            if (!(deserializedObject instanceof List<?>)) {
                LOGGER.warn("Dados desserializados não são uma lista: {}", serializedItems);
                return new ArrayList<>();
            }

            List<?> rawList = (List<?>) deserializedObject; // Explicit cast
            List<ItemStack> itemStacks = new ArrayList<>();

            for (Object obj : rawList) {
                if (obj instanceof ItemStack) {
                    itemStacks.add((ItemStack) obj);
                }
            }

            return itemStacks;
        } catch (IOException | ClassNotFoundException e) {
            throw new ItemSerializationException("Erro ao desserializar string de tamanho " + serializedItems.length() + ": " + e.getMessage(), e);
        }
    }


    public static class ItemSerializationException extends RuntimeException {
        public ItemSerializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}