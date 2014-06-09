package controllers;

import play.*;
import play.mvc.*;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.data.DynamicForm;
import play.data.Form;

import models.Image;
import models.Request;

import views.html.*;

import java.io.File;
import java.util.Map;

import com.avaje.ebean.Ebean;

public class Application extends Controller {
  static Form<Image> imageForm = Form.form(Image.class);

  public static Result index() {
    return ok(views.html.index.render(Image.all(), imageForm, Request.all()));
  }

  public static Result upload() {
    MultipartFormData body = request().body().asMultipartFormData();
    String[] params = { "tag" };
    DynamicForm input = Form.form();
    input = input.bindFromRequest(params);
    String tag = input.data().get("tag");
    FilePart picture = body.getFile("imageName");
    if (picture != null) {
      String fileName = picture.getFilename();
      String contentType = picture.getContentType();
      File file = picture.getFile();
      if ( contentType.startsWith("image/") ) {
        String fullPath = Play.application().path().getPath() + "/public/images/upload/";
        file.renameTo(new File(fullPath, fileName));
        Image image = new Image();
        image.tag = tag;
        image.imageName = fileName;
        Image.add(image);
      }
      return redirect(routes.Application.index());
    } else {
      flash("error", "Missing file");
      return redirect(routes.Application.index());
    }
  }

  public static Result publish(String tag, String key) {
    String imageName = null;
    int status;
    if (Image.hasTag(tag)) {
      String requestKey = tag + " " + key;
      Ebean.beginTransaction();
      try {
        if (Request.hasKey(requestKey) == false) {
          Request request = new Request();
          request.requestKey = requestKey;
          request.imageId = Image.getRandomImageId(tag);
          Request.add(request);
        }
        Ebean.commitTransaction();
      } catch (Exception ex) {
        Ebean.rollbackTransaction();
        throw new RuntimeException(ex);
      } finally {
        Ebean.endTransaction();
      }
      imageName = Request.getImageName(requestKey);
      status = 200;
    } else {
      imageName = "404.jpg";
      status = 404;
    }
    String dirPath = Play.application().path().getPath() + "/public/images/upload";
    File file = new File(dirPath + "/" + imageName);
    return status(status, file);
  }
}
