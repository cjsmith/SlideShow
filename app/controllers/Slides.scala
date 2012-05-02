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

case class Slide(id: Long, markdown: String, position: Int)

object Slides extends Controller {

  import views.html.Slides._

  val slideForm = Form(
    mapping(
      "id" -> longNumber,
      "markdown" -> nonEmptyText,
      "position" -> number(min = 0, max = 500))(Slide.apply)(Slide.unapply))

  def all(implicit c: Connection) = SQL("select * from Slide order by position")().map{ row => 
    Slide(row[Long]("id"), row[String]("markdown"), row[Int]("position"))
  }.toList

  def index = Action {
    DB.withConnection { implicit c => 
      Ok(views.html.Slides.slideshow())
    }
  }
 
  def edit = Action {
    DB.withConnection { implicit c =>
      Ok(views.html.Slides.editor(all, slideForm))
    }
  }
  
  def slides = Action {
    DB.withConnection { implicit c => 
      Ok(generate(all)).as("application/json") 
    }
  }
  
  def create = Action { implicit req =>
    DB.withConnection { implicit c =>
      slideForm.bindFromRequest.fold(
        slideFormWithErrors => BadRequest(views.html.Slides.editor(all, slideFormWithErrors)),
        slide => {
          SQL("insert into Slide (markdown, position) values ({markdown},{position})").on(
            'markdown -> slide.markdown,
            'position -> slide.position).executeUpdate()
          Redirect(routes.Slides.index)
        })
    }
  }

  def update(id: Long) = Action { implicit req =>
    DB.withConnection { implicit c =>
      req.body.asJson.map { json =>
  	    val slide = com.codahale.jerkson.Json.parse[Slide](Json.stringify(json))
	    SQL("update Slide set markdown={markdown}, position={position} where id={id}").on(
	        'id -> id,
	        'markdown -> slide.markdown,
	        'position -> slide.position).executeUpdate()
          Redirect(routes.Slides.edit)
	}.getOrElse {
	  println("bad request - no json")
	  BadRequest("Expecting Json data")
	} 
    }
  }

  def delete(id: Long) = Action {
    DB.withConnection { implicit c =>
      SQL("delete from Slide where id={id}").on(
        'id -> id).executeUpdate()
      Redirect(routes.Slides.index)
    }
  }

  def updatePositions = Action { req =>
	println("updating positions...")
  	req.body.asJson.map { json =>
	  json.asOpt[Map[String, Int]].map { slidePositions =>
	    for((slideId, position) <- slidePositions) {
	        println("updating " + slideId + " to position" + position) 
		    DB.withConnection { implicit c =>
	          SQL("update Slide set position={position} where id={id}").on(
	            'id-> slideId,
	            'position -> position).executeUpdate()
		    
		    }
	    }
	  	Ok("positions updated")
  	  }.getOrElse {
  		  println("bad request => couldn't parse")
  		  BadRequest("Expecting Json data")
  	  }
	}.getOrElse {
	  println("bad request - no json")
	  BadRequest("Expecting Json data")
	}
  }

}
