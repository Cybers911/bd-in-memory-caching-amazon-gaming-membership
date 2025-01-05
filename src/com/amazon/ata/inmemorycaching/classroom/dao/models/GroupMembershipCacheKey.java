package com.amazon.ata.inmemorycaching.classroom.dao.models;

import java.util.Objects;

//This is the key for accesing the cache for group memberships.
//Since it required two values (userId and groupId), we have to create a custom key class.
//We need the class to hold the values
//This class is an immutable with final instance data no setters, and 'equals' method to compare two keys.
// In this case, we are treating the userId and groupId as final fields.
// No need to do defense copy or defense return
//Beign immutable means can be used in multi-threaded environments or concurrent processing
// without worrying about thread safety.


public final class GroupMembershipCacheKey {

    private final String userId;
    private final String groupId;
// Treating as final A method that neve change the data inside the key, so it's immutable. '
    public GroupMembershipCacheKey(final String userId, final String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        GroupMembershipCacheKey request = (GroupMembershipCacheKey) obj;

        return userId.equals(request.userId) && groupId.equals(request.groupId);
    }
}
