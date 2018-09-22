package org.pispeb.treffpunkt.server.service.impl;

import org.pispeb.treffpunkt.server.service.api.UsergroupAPI;
import org.pispeb.treffpunkt.server.service.domain.Event;
import org.pispeb.treffpunkt.server.service.domain.Usergroup;

import javax.jws.WebService;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebService(endpointInterface = "org.pispeb.treffpunkt.server.service.api.UsergroupAPI")
@Path("/groups")
public class UsergroupImpl extends ServiceImpl implements UsergroupAPI {

    @Override
    public Usergroup getTest() {
        Usergroup ug = new Usergroup();
        ug.setName("n4m3");
        List<Integer> memberIDs = new ArrayList<>();
        List<Integer> eventIDs = new ArrayList<>();
        memberIDs.add(1);
        eventIDs.add(2);
        ug.setMemberIDs(memberIDs);
        ug.setEventIDs(eventIDs);

        return ug;
    }

    @Override
    public void test(int gid, Usergroup ug) {
        int i = 0;
    }

    @Override
    public int create(Usergroup group) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void edit(int gid) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void addMembers(int gid, int[] memberIds) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void removeMembers(int gid, int[] memberIds) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void leaveGroup(int gid) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void addEvent(int gid, Event event) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
