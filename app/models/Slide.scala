package models

import play.api.Play.current
import play.api.db._
import anorm._

case class Slide(id: Long, markdown: String, position: Int)

object Slide {
  
  def all = DB.withConnection{ implicit c => 
	SQL("select * from Slide order by position")().map{ row => 
	  Slide(row[Long]("id"), row[String]("markdown"), row[Int]("position"))
	}.toList
  } 

  def insert(slide:Slide) = DB.withConnection { implicit c => 
    SQL("insert into Slide (markdown, position) values ({markdown},{position})").on(
      'markdown -> slide.markdown,
      'position -> slide.position).executeUpdate()
  }

  def update(slide:Slide) = DB.withConnection { implicit c => 
    SQL("update Slide set markdown={markdown}, position={position} where id={id}").on(
    'id -> slide.id,
    'markdown -> slide.markdown,
    'position -> slide.position).executeUpdate()
  }
 
  def delete(id:Long) = {
	  DB.withConnection { implicit c =>
	      SQL("delete from Slide where id={id}").on(
	        'id -> id).executeUpdate()
	  }
  }
  
  def updatePositions(slidePositions:Map[String, Int]) {
    for((slideId, position) <- slidePositions) {
	    DB.withConnection { implicit c =>
          SQL("update Slide set position={position} where id={id}").on(
            'id-> slideId,
            'position -> position).executeUpdate()
	    
	    }
    }
  }
}