package com.tycorp.eb.app.meta_data;

import com.tycorp.eb.app.AbstractEbAppAggregateService;
import com.tycorp.eb.domain.meta_data.model.Tag;
import com.tycorp.eb.domain.meta_data.model.Ticker;
import com.tycorp.eb.domain.meta_data.repository.TagRepository;
import com.tycorp.eb.domain.meta_data.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AbstractMetaDataService extends AbstractEbAppAggregateService {

    @Autowired
    protected TickerRepository tickerRepo;
    @Autowired
    protected TagRepository tagRepo;


    public AbstractMetaDataService() { }


    protected Set<Ticker> findTickersByNameOrCreate(Set<String> names){
        return names.stream().map(name ->
                tickerRepo.save(
                        tickerRepo.findByName(name).orElseGet(() -> tickerRepo.save(new Ticker(name)))))
                .collect(Collectors.toSet());
    }

    protected Set<Tag> findTagsByNameOrCreate(Set<String> names){
        return names.stream().map(name ->
                tagRepo.save(
                        tagRepo.findByName(name).orElseGet(() -> tagRepo.save(new Tag(name)))))
                .collect(Collectors.toSet());
    }

}
