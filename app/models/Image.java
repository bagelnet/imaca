package models;

import java.util.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.validation.Constraints.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlRow;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "image_name" })})
public class Image extends Model {
  @Id
  public Long id;

  @Required
  @Column(nullable = false)
  public String tag;

  @Column(nullable = false, name = "image_name")
  public String imageName;

  public static Finder<Long, Image> find = new Finder(
    Long.class, Image.class
  );

  public static Map<String,String> options() {
    LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
    String[] list = {"wara", "oko", "naki", "raku"};
    for(String tag: list) options.put(tag, tag);
    return options;
  }
  
  public static List all() {
    return find.all();
  }

  public static void add(Image image) {
    image.save();
  }

  public static boolean hasTag(String tag) {
    Query<Image> query = find.where("tag=:tag").setParameter("tag", tag);
    if(query.findRowCount() > 0) return true;
    else return false;
  }

  public static Long getRandomImageId(String tag) {
    String sql = "SELECT id FROM Image WHERE id=(SELECT id FROM Image WHERE tag=:tag ORDER BY random() LIMIT 1)";
    List<SqlRow> sqlRows = new ArrayList<SqlRow>();
    while(sqlRows.size() == 0) {
      sqlRows = Ebean.createSqlQuery(sql).setParameter("tag", tag).findList();
    }
    return new Long(sqlRows.get(0).get("id").toString()).longValue();
  }
}