# Users schema
 
# --- !Ups
INSERT INTO Slide (`id`,`markdown`,`position`)
VALUES
  (1, 'introducing\n\n![play][1]\n\n[1]:http://www.playframework.org/assets/images/logos/normal.png\n\nColin Smith\n\nJustin Long', 1),
  (171, '**Outline**\n\n* intro\n\n* demo\n\n* architecture\n\n* features\n\n* scala-isms\n\n* gotchas\n', 2),
  (61, '**Play2** vs Play1.x\n\n* **scala** core\n\n* joined the **typesafe** stack\n\n* **akka** integration (replaces jobs)\n\n* **sbt** build mgmt\n', 3),
  (21, '**Constituent \nIngredients**\n![enter image description here][1]\n\n\n  [1]: http://teehanlax.com.s3.amazonaws.com/wp-content/uploads/beakers.jpg', 7),
  (31, '**Demo**\n![enter image description here][1]\n\n\n  [1]: http://1.bp.blogspot.com/_sLH5Gddzgn0/Sm6WtLhU_QI/AAAAAAAAAJ4/q9SIlKN4op0/s400/car20.jpg', 4),
  (41, '**SBT**\n\n    play new app\n    play run //dev mode\n    play ~run 8888\n    play start //prod\n    play eclipsify\n    play test\n\n\n* ivy/maven dependencies \n', 8),
  (51, '**akka**\n\n    def index = Action {\n      Async {\n        Akka.future { longComputation() }.map { result =>\n          Ok(\"Got \" + result)    \n        }    \n      }\n    }', 11),
  (71, '\n * MVC\n\n * Netty for HTTP\n\n * [akka services requests][1]\n\n  [1]: https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/scala/play/core/system/Invoker.scala', 6),
  (81, '**Features**\n\n* comet/web sockets\n\n* cache API (EHCache)\n\n*  wrappers for jackson, akka, AsyncHTTP, OAuth, etc\n', 9),
  (91, '**WebSockets**\n\n    def index = WebSocket.using[String] { request => \n      \n      // Log events to the console\n      val in = Iteratee.foreach[String](println).mapDone { _ =>\n        println(\"Disconnected\")\n      }\n      \n      // Send a single \'Hello!\' message\n      val out = Enumerator(\"Hello!\")\n      \n      (in, out)\n    }', 12),
  (101, '**Questions?**\n![enter image description here][1]\n\n\n  [1]: http://imgs.xkcd.com/comics/airfoil.png', 17),
  (111, '**Gotchas**\n![enter image description here][1]\n\n\n  [1]: http://www.threadbombing.com/data/media/54/john_stewart_facepalm.jpg', 14),
  (121, '* no war packaging yet\n\n* stable but immature\n\n* roll your own auth\n\n* no built in mailer\n\n', 15),
  (131, '* performance isn\'t amazing\n\n* compilation time increases with app size\n\n* no Socket.IO\n\n* OWASP vulnerabilities', 16),
  (151, '**Scala-isms:**\n\n     def create = Action { implicit req =>\n        slideForm.bindFromRequest.fold(\n     //...\n\nSee \n\n* [Action][1]\n\n* [BodyParsers][2]\n\n\n  [1]: https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/scala/play/api/mvc/Action.scala\n  [2]: https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/scala/play/api/mvc/ContentTypes.scala', 13),
  (161, '**More Features**\n\n* modules\n\n* CoffeeScript\n\n* Less\n\n* Heroku', 10),
  (181, '**Architecture**\n\n![enter image description here][1]\n\n\n  [1]: http://upload.wikimedia.org/wikipedia/commons/8/89/Peckham_library_1.jpg', 5),
  (191, '**Resources:**\n\n[playframework.org][1]\n[play2-slideshow.heroku.com][2]\n[github.com/cjsmith/SlideShow][3]\n[alloyengine.com][4]\n\n\n  [1]: http://www.playframework.org\n  [2]: http://play2-slideshow.heroku.com\n  [3]: https://github.com/cjsmith/SlideShow\n  [4]: https://alloyengine.com', 18);
 
# --- !Downs
 
DELETE FROM Slide;
