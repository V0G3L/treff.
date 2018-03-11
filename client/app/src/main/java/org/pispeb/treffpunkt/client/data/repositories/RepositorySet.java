package org.pispeb.treffpunkt.client.data.repositories;

/**
 * A simple POJO containing all currently used repositories.
 */
public class RepositorySet {

    public final ChatRepository chatRepository;
    public final EventRepository eventRepository;
    public final UserGroupRepository userGroupRepository;
    public final UserRepository userRepository;

    public RepositorySet(ChatRepository chatRepository,
                         EventRepository eventRepository,
                         UserGroupRepository userGroupRepository,
                         UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }
}
