package models;

import java.util.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlRow;


@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "request_key" })})
public class Request extends Model {
  @Id
  public Long id;

  @Column(nullable = false, name = "request_key")
  public String requestKey;

  @Column(nullable = false, name = "image_id")
  public Long imageId;
  
  public static Finder<Long,Request> find = new Finder(
    Long.class, Request.class
  );

  public static List all() {
    return find.all();
  }

  public static boolean hasKey(String key) {
    Query<Request> query = find.where("request_key=:key").setParameter("key", key);
    if(query.findRowCount() > 0) return true;
    else return false;
  }

  
  public static void add(Request request) {
    request.save();
  }
  
  public static String getImageName(String requestKey) {
    String sql = "SELECT image_name FROM image WHERE id=(SELECT image_id FROM request WHERE request_key = :key) LIMIT 1";
    List<SqlRow> sqlRows = Ebean.createSqlQuery(sql).setParameter("key", requestKey).findList();
    return (String) sqlRows.get(0).get("image_name");
  }
}