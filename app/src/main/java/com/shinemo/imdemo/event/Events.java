package com.shinemo.imdemo.event;

public interface Events {
    class ConversationUpdateEvent {}

    class OffMessageEvent {

    }

    class GroupNameChangeEvent{
        public GroupNameChangeEvent(String name) {
            this.name = name;
        }
        public String name;
    }
}