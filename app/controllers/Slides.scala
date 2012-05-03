package controllers

import play.api._
import play.api.data._
import play.api.data.format.Formats._
import play.api.mvc._
import play.api.db._
import anorm._
import play.api.Play.current
import com.codahale.jerkson.Json._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import java.sql.Connection
import play.api.libs.json.Json
import models.Slide

object Slides extends Controller {

  import views.html.Slides._

  val slideForm = Form(
    mapping(
      "id" -> longNumber,
      "markdown" -> nonEmptyText,
      "position" -> number(min = 0, max = 500))(Slide.apply)(Slide.unapply)
  )

  def index = Action {
     Ok(views.html.Slides.slideshow())
  }
 
  def edit = Action {
     Ok(views.html.Slides.editor(models.Slide.all, slideForm))
  }
  
  def slides = Action {
    Ok(generate(models.Slide.all)).as("application/json") 
  }
  
  def create = Action { implicit req =>
    slideForm.bindFromRequest.fold(
      slideFormWithErrors => BadRequest(views.html.Slides.editor(models.Slide.all, slideFormWithErrors)),
      slide => {
        Slide.insert(slide)
        Redirect(routes.Slides.edit)
      }
    ) 
  }

  def update(id: Long) = Action { implicit req =>
    req.body.asJson.map { json =>
  	    val slide = com.codahale.jerkson.Json.parse[Slide](Json.stringify(json))
  	    Slide.update(slide)
        Ok("Slide updated")
	}.getOrElse {
	  println("bad request - no json")
	  BadRequest("Expecting Json data")
    }
  }

  def delete(id: Long) = Action {
    Slide.delete(id)
    Ok("slide deleted")
  }

  def updatePositions = Action { req =>
  	req.body.asJson.map { json =>
	  json.asOpt[Map[String, Int]].map { slidePositions =>
	    Slide.updatePositions(slidePositions)
	  	Ok("positions updated")
  	  }.getOrElse {
  		  BadRequest("Expecting Json data")
  	  }
	}.getOrElse {
	  println("bad request - no json")
	  BadRequest("Expecting Json data")
	}
  }

}
