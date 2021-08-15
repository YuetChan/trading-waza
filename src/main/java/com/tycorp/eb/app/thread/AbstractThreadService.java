package com.tycorp.eb.app.thread;

import com.tycorp.eb.app.meta_data.AbstractMetaDataService;
import com.tycorp.eb.domain.thread.repository.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractThreadService extends AbstractMetaDataService {

    @Autowired
    protected ThreadRepository threadRepo;

}
