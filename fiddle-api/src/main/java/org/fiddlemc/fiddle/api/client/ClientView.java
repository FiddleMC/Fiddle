package org.fiddlemc.fiddle.api.client;

/**
 * This class represents the static circumstances under which a client (typically a player) observes the data sent.
 * <p>
 * An instance of this class represents a set of values that are static within a client's session with the server:
 * i.e. they stay the same for a client during the entire session.
 * Note that for the purposes of this class, the session of the client only starts when it is able to have any
 * view of relevant data sent to it. For example, when a player joins, we first present them with the option
 * to accept or decline the resource pack. Until it has been confirmed that the resource pack has been definitively
 * loaded (so the moment after accepting, downloading and successful loading) or not loaded (so the moment it is either
 * confirmed that the player declined the resource pack, that the download failed or that another type of error
 * occurred after which we can be sure the resource pack will not be successfully loaded anymore), that player is not
 * assigned their {@link ClientView} yet. This also implies that we must make sure not to send any relevant
 * data (such as chunks, entities and potentially even the player's inventory - although that can be sent
 * according to a fallback view and updated as necessary) to the player before their view
 * is determined.
 * <br>
 * Apart from the interval before having definitely loaded or not loaded the resource pack, a player's session
 * (and as such their {@link ClientView} value) lasts from joining the server until disconnecting.
 * </p>
 * <p>
 * This class may be extended to support additional values relevant to certain views
 * (such as the protocol version of the client).
 * </p>
 */
public interface ClientView {

    AwarenessLevel getAwarenessLevel();

    /**
     * @return True only if this client understands all server-side translatables
     * (such as in the case of a client that has loaded the resource pack containing them),
     * false if it can not be guaranteed.
     */
    default boolean understandsAllServerSideTranslatables() {
        return false;
    }

    /**
     * @return True only if this client understands all server-side items
     * (such as in the case of a client mod that has added them to its client-side registry),
     * false if it can not be guaranteed.
     */
    default boolean understandsAllServerSideItems() {
        return false;
    }

    /**
     * @return True only if this client understands all server-side blocks
     * (such as in the case of a client mod that has added them to its client-side registry),
     * false if it can not be guaranteed.
     */
    default boolean understandsAllServerSideBlocks() {
        return false;
    }

    /**
     * This enum represents the major categorization of the client's capability
     * to interpret data sent by the server.
     */
    enum AwarenessLevel {
        /**
         * For Java clients that have not accepted the server resource pack,
         * and also do not have the client mod.
         * <p>
         * This generally results in data being replaced by the closest or most acceptable vanilla equivalent,
         * with additional rendering potentially being done through the use of vanilla entities.
         * </p>
         */
        JAVA_DEFAULT,

        /**
         * For Java clients that have accepted the server resource pack,
         * but do not have the client mod.
         * <p>
         * This generally results in data being replaced by hosts that are overridden in the resource pack
         * (such as block states) or having additional data attached that links to the resource pack
         * (such as explicit item model).
         * Additional rendering can be done through the use of entities.
         * </p>
         */
        JAVA_WITH_RESOURCE_PACK, // TODO implement

        /**
         * For Java clients that are have the client mod, i.e. they have the mod installed and are able to use
         * a sufficiently up-to-date version of it.
         * <p>
         * This generally results in data being sent as-is, because when joining the server, the client receives
         * the necessary information to interpret the server-side block and item keys directly from then on.
         * </p>
         */
        JAVA_WITH_CLIENT_MOD // TODO implement
    }

}
