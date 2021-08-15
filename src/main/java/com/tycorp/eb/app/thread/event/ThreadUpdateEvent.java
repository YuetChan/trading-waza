package com.tycorp.eb.app.thread.event;

import com.tycorp.eb.app.AbstractEbAppAggregateEvent;
import com.tycorp.eb.app.thread.dto.non_exposable.ThreadUpdateDto;
import com.tycorp.eb.domain.user.model.LoginedEbUserDetail;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
public class ThreadUpdateEvent extends AbstractEbAppAggregateEvent {

    private ThreadUpdateDto updateDto;
    private Long occuredAt = generateOccuredAt();


    public ThreadUpdateEvent(LoginedEbUserDetail loginedEbUserDetail,
                             Object source, ThreadUpdateDto updateDto, String UUID) {
        super(source);
        this.loginedEbUserDetail = loginedEbUserDetail;

        this.updateDto = updateDto;
        this.UUID = UUID;
    }


    public static ThreadUpdateEvent.Builder getBuilder() {
        return new ThreadUpdateEvent.Builder();
    }
    public static class Builder {

        private LoginedEbUserDetail loginedEbUserDetail;
        public Builder setOperator(LoginedEbUserDetail loginedEbUserDetail) {
            this.loginedEbUserDetail = loginedEbUserDetail;
            return this;
        }


        private Object source;

        private ThreadUpdateDto updateDto;
        private String UUID;

        public Builder setSource(Object source) {
            this.source = source;
            return this;
        }

        public Builder setUpdateDto(ThreadUpdateDto updateDto) {
            this.updateDto = updateDto;
            return this;
        }
        public Builder setUUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public ThreadUpdateEvent build() {
            return new ThreadUpdateEvent(loginedEbUserDetail, source, updateDto, UUID);
        }
    }

}

