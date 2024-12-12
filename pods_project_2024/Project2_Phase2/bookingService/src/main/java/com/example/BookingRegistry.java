package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.http.javadsl.Http;
import java.util.*;
import akka.cluster.sharding.typed.javadsl.EntityRef;


public class BookingRegistry extends AbstractBehavior<BookingRegistry.Command>  {
  public static Map<Integer,EntityRef<ShowActor.Command>> showActors =new HashMap<>();
  public static final List<Theatre> theatres=new ArrayList<>();
  public Integer count;
  sealed interface Command  extends CborSerializable{}


  public final record Theatre(Integer id, String name , String location) implements CborSerializable{}
  public final record User(Integer id,String name, String email)implements CborSerializable{}
  public final record Theatres(List<Theatre> theatres)implements CborSerializable{}
  public final record Booking(Integer id,Integer user_id,Integer show_id , Integer seats_booked) implements CborSerializable{}
  public final record Bookings(List<Booking> bookings)implements CborSerializable{}



  public final record GetTheatre(ActorRef<Theatres> replyTo) implements Command{}
  public final record GetUserBookings(Integer user_id, ActorRef<Bookings> replyTo) implements  Command{}
  public final record GetShow(Integer id,ActorRef<ShowActor.Show> replyTo) implements Command{ }
  public final record ShowByTheatre(Integer theatre_id,ActorRef<WorkerActor.Showlist> replyTo) implements  Command{}

  public final record AddBooking(Booking booking,ActorRef<ShowActor.Booking> replyTo) implements  Command{}

  public final record DeleteAllUserBookings(Integer user_id, ActorRef<ShowActor.Response> replyTo) implements  Command{}
  public final record DeleteAllBookings(ActorRef<WorkerActor.DeleteResponse> replyTo) implements Command {}
  public final record DeleteUserBooking( Integer user_id,Integer show_id, ActorRef<ShowActor.Response> replyTo) implements Command{}

  public ActorRef<WorkerActor.Command> router;

  private BookingRegistry(ActorContext<Command> context, ActorRef<WorkerActor.Command> router,Map<Integer,EntityRef<ShowActor.Command>> showActors) {
    super(context);
    this.router = router;
   this.showActors = showActors;
   count=0;
    String[] theatresList = { "1,Helen Hayes Theater,240 W 44th St.",
           "2,Cherry Lane Theatre,38 Commerce Street",
           "3,New World Stages,340 West 50th Street",
         "4,The Zipper Theater,100 E 17th St",
           "5,Queens Theatre,Meadows Corona Park",
           "6,The Public Theater,425 Lafayette St",
           "7,Manhattan Ensemble Theatre,55 Mercer St.",
           "8,Metropolitan Playhouse,220 E 4th St.",
            "9,Acorn Theater,410 West 42nd Street",
            "10,Apollo Theater,253 West 125th Street" };

    for (String line : theatresList) {
      String[] str = line.split(","); // use comma as separator
      int id = Integer.parseInt(str[0]);
      String name = str[1];
     String location = str[2];
     theatres.add(new Theatre(id, name, location));
    }
  
    }
  


  public static Behavior<Command> create(ActorRef<WorkerActor.Command> router,Map<Integer,EntityRef<ShowActor.Command>> showActors) {

    return Behaviors.setup(context->(new BookingRegistry(context, router,showActors)));
  }

  @Override
  public Receive<Command> createReceive() {
    return newReceiveBuilder()
        .onMessage(GetTheatre.class, this::onGetTheatre)
        .onMessage(GetShow.class, this::onGetShow)
        .onMessage(AddBooking.class,this::onAddBooking)
        .onMessage(DeleteUserBooking.class,this::onDeleteUserBooking)
        .onMessage(GetUserBookings.class,this::onGetUserBookings)
        .onMessage(ShowByTheatre.class,this::onShowByTheatre)
        .onMessage(DeleteAllUserBookings.class,this::onDeleteAllUserBookings)
        .onMessage(DeleteAllBookings.class,this::onDeleteAllBookings)
        .build();
  }

  private Behavior<Command> onGetTheatre(GetTheatre message) {
    message.replyTo().tell(new Theatres(Collections.unmodifiableList(new ArrayList<>(theatres))));
    return this;
  }
  private Behavior<Command> onGetShow(GetShow message) {
    boolean isInvalidShow =message.id>20||message.id<1;
    if(isInvalidShow)
      message.replyTo.tell(new ShowActor.Show(-1,-1,null,-1,-1));
    else 
    {
      Integer show_id = message.id;
      EntityRef<ShowActor.Command> ref = showActors.get(show_id);
      ref.tell(new ShowActor.GetShow(message.replyTo));
    }
    return this;
  }

  private Behavior<Command> onAddBooking(AddBooking message) {
    final Http http = Http.get(getContext().getSystem());
    Integer user_id = message.booking.user_id;
    Integer show_id = message.booking.show_id;
    Integer seats_booked = message.booking.seats_booked;
    boolean isValidUser = UserService.isUser(user_id,http);
    boolean isValidShow = show_id > 0 && show_id < 21;
    if(isValidShow && isValidUser)
      processValidBooking(message, user_id, show_id, seats_booked);
    else 
      message.replyTo.tell(new ShowActor.Booking(-1, -1, -1, -1));

    return this;
}

private void processValidBooking(AddBooking message, Integer user_id, Integer show_id, Integer seats_booked) {
    this.count = this.count + 1;
    ActorRef<ShowActor.Booking> replyTo = message.replyTo;
    EntityRef<ShowActor.Command> ref = showActors.get(show_id);
    ref.tell(new ShowActor.AddBooking(new ShowActor.Booking(count, user_id, show_id, seats_booked), replyTo));
}
                           
private Behavior<Command> onDeleteUserBooking(DeleteUserBooking message) {
  boolean isInvalidShow = message.show_id > 20 || message.show_id < 1;

  if (isInvalidShow) {
      message.replyTo.tell(new ShowActor.Response(false));
  } else {
      showActors.get(message.show_id).tell(new ShowActor.DeleteUserBooking(message.user_id, message.show_id, message.replyTo, null));
  }

  return this;
}

  private Behavior<Command> onDeleteAllUserBookings(DeleteAllUserBookings message) {
    this.router.tell(new WorkerActor.DeleteAllUserBookings(message.replyTo, message.user_id));
    return this;
  }

  private Behavior<Command> onGetUserBookings(GetUserBookings message) {
    this.router.tell(new WorkerActor.GetUserBookings(message.replyTo, message.user_id));
    return this;
  }

  private Behavior<Command> onShowByTheatre(ShowByTheatre message) {
    boolean isInvalidTheatre = message.theatre_id>10||message.theatre_id<1;
    if(isInvalidTheatre)
        message.replyTo.tell(new WorkerActor.Showlist(null));
    else{
      this.router.tell(new WorkerActor.ShowByTheatre(message.theatre_id, message.replyTo));
    }
    return this;
  }

  private Behavior<Command> onDeleteAllBookings(DeleteAllBookings message) {
    this.router.tell(new WorkerActor.DeleteAllBookings(message.replyTo));
    return this;
  }


}
