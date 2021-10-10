package com.tycorp.tw.rest.event;

import com.tycorp.tw.rest.dto.non_exposable.RowCreateDto;
import com.tycorp.tw.domain.SignedInUserDetail;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;

@Getter(AccessLevel.PUBLIC)
public class RowCreateEvent extends AbstractAppEvent {

    private RowCreateDto createDto;
    private Long occuredAt = Instant.now().toEpochMilli();

    public RowCreateEvent(SignedInUserDetail signedInUserDetail,
                          String UUID,
                          Object source, RowCreateDto createDto) {
        super(source);
        this.signedInUserDetail = signedInUserDetail;
        this.UUID = UUID;

        this.createDto = createDto;
    }

    public static RowCreateEvent.Builder getBuilder() {
        return new RowCreateEvent.Builder();
    }
    public static class Builder {

        private SignedInUserDetail signedInUserDetail;
        public RowCreateEvent.Builder setOperator(SignedInUserDetail signedInUserDetail) {
            this.signedInUserDetail = signedInUserDetail;
            return this;
        }

        private Object source;
        private String UUID;

        private RowCreateDto createDto;

        public RowCreateEvent.Builder setSource(Object source) {
            this.source = source;
            return this;
        }

        public RowCreateEvent.Builder setUUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public RowCreateEvent.Builder setCreateDto(RowCreateDto createDto) {
            this.createDto = createDto;
            return this;
        }

        public RowCreateEvent build() {
            return new RowCreateEvent(signedInUserDetail, UUID, source, createDto);
        }

    }

}
