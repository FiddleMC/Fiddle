package org.fiddlemc.fiddle.api.bukkit.enuminjection;

/**
 * A class that injects new values into a Bukkit enum {@link E}
 * that correspond to values from another source (of type {@link T}).
 */
public interface BukkitEnumSynchronizer<E extends Enum<E>, T> {
}
