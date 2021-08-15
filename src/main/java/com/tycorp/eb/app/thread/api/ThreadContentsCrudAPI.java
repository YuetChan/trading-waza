package com.tycorp.eb.app.thread.api;

import com.tycorp.eb.app.thread.AbstractThreadService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/threads")
public class ThreadContentsCrudAPI extends AbstractThreadService {

//    @PersistenceContext
//    protected EntityManager em;
//
//    @GetMapping(value = "/{threadId}/contents/{pageNum}", produces = "application/json")
//    public ResponseEntity<JsonObject> threadContentsGetByPageNum(@PathVariable(name = "threadId") long p_threadId,
//                                                                 @PathVariable(name = "pageNum") long p_pageNum) {
//        var qry = em.createNativeQuery(
//                "SELECT tc.content FROM thread t, thread_contents tc " +
//                        "WHERE tc.thread_id = :thread_id " +
//                        "AND tc.contents_order = :page_num AND t.thread_id = tc.thread_id AND t.is_active = 1");
//        qry.setParameter("thread_id", p_threadId);
//        qry.setParameter("page_num", p_pageNum);
//
//        try {
//            var resJson = DefaultGsonHelper.getJsonObject();
//            resJson.addProperty("content", qry.getSingleResult().toString());
//            return new ResponseEntity(resJson, HttpStatus.OK);
//        } catch (NoResultException nre){
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        }
//    }
//
//    @GetMapping(value = "/{threadId}/contents/count", produces = "application/json")
//    public ResponseEntity<JsonObject> threadContentsCountGet(@PathVariable(name = "threadId") long p_threadId) {
//        var qry = em.createNativeQuery(
//                "SELECT COUNT(*) FROM thread t, thread_contents tc " +
//                        "WHERE tc.thread_id = :thread_id " +
//                        "AND t.thread_id = tc.thread_id AND t.is_active = 1 " +
//                        "GROUP BY(tc.thread_id)");
//        qry.setParameter("thread_id", p_threadId);
//
//        try {
//            var resJson= DefaultGsonHelper.getJsonObject();
//            resJson.addProperty("count", Integer.parseInt(qry.getSingleResult().toString()));
//            return new ResponseEntity(resJson, HttpStatus.OK);
//        } catch (NoResultException nre){
//            return new ResponseEntity("Thread Not Found", HttpStatus.NOT_FOUND);
//        }
//    }

}
