package com.tycorp.eb.app.thread.event;

import com.tycorp.eb.app.AbstractEbAppAggregateEvent;
import com.tycorp.eb.app.thread.dto.non_exposable.ThreadCreateDto;
import com.tycorp.eb.domain.user.model.LoginedEbUserDetail;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
public class ThreadCreateEvent extends AbstractEbAppAggregateEvent {

    private ThreadCreateDto createDto;
    private Long occuredAt = generateOccuredAt();


    public ThreadCreateEvent(LoginedEbUserDetail loginedEbUserDetail,
                             Object source, ThreadCreateDto createDto, String UUID) {
        super(source);
        this.loginedEbUserDetail = loginedEbUserDetail;

        this.createDto = createDto;
        this.UUID = UUID;
    }


    public static ThreadCreateEvent.Builder getBuilder() {
        return new ThreadCreateEvent.Builder();
    }
    public static class Builder {

        private LoginedEbUserDetail loginedEbUserDetail;
        public ThreadCreateEvent.Builder setOperator(LoginedEbUserDetail loginedEbUserDetail) {
            this.loginedEbUserDetail = loginedEbUserDetail;
            return this;
        }

        private Object source;

        private ThreadCreateDto createDto;
        private String UUID;

        public ThreadCreateEvent.Builder setSource(Object source) {
            this.source = source;
            return this;
        }

        public ThreadCreateEvent.Builder setCreateDto(ThreadCreateDto createDto) {
            this.createDto = createDto;
            return this;
        }
        public ThreadCreateEvent.Builder setUUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public ThreadCreateEvent build() { return new ThreadCreateEvent(loginedEbUserDetail, source, createDto, UUID); }

    }

}
