package com.tycorp.tw.rest.event;

import com.tycorp.tw.domain.SignedInUserDetail;
import com.tycorp.tw.rest.dto.non_exposable.RowCreateDto;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;

@Getter(AccessLevel.PUBLIC)
public class RowsBatchCreateEvent extends AbstractAppEvent {

    private RowCreateDto[] createDtos;
    private Long occuredAt = Instant.now().toEpochMilli();

    public RowsBatchCreateEvent(SignedInUserDetail signedInUserDetail,
                          String UUID,
                          Object source, RowCreateDto[] createDtos) {
        super(source);
        this.signedInUserDetail = signedInUserDetail;
        this.UUID = UUID;

        this.createDtos = createDtos;
    }

    public static RowsBatchCreateEvent.Builder getBuilder() {
        return new RowsBatchCreateEvent.Builder();
    }
    public static class Builder {

        private SignedInUserDetail signedInUserDetail;
        public RowsBatchCreateEvent.Builder setOperator(SignedInUserDetail signedInUserDetail) {
            this.signedInUserDetail = signedInUserDetail;
            return this;
        }

        private Object source;
        private String UUID;

        private RowCreateDto[] createDtos;

        public RowsBatchCreateEvent.Builder setSource(Object source) {
            this.source = source;
            return this;
        }

        public RowsBatchCreateEvent.Builder setUUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public RowsBatchCreateEvent.Builder setCreateDtos(RowCreateDto[] createDtos) {
            this.createDtos = createDtos;
            return this;
        }

        public RowsBatchCreateEvent build() {
            return new RowsBatchCreateEvent(signedInUserDetail, UUID, source, createDtos);
        }

    }
}
