package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import java.util.*;




public class WorkerActor extends AbstractBehavior<WorkerActor.Command>  {
    public Map<Integer,EntityRef<ShowActor.Command>> showActors;
    public Integer user_id;
    public ActorRef<BookingRegistry.Bookings> replyTo;
    public ActorRef<ShowActor.Response> replyToDelete;
    public ActorRef<WorkerActor.Command> selfRef;

    public ActorRef<WorkerActor.DeleteResponse> replyToDeleteAll;
    public Integer numProcessed;
    public boolean flag;
    List<BookingRegistry.Booking> userBookings = new ArrayList<>();
    List<ShowActor.Show> showOfTheatre =new ArrayList<>();

    sealed interface Command extends CborSerializable{}
    public final record GetUserBookings(ActorRef<BookingRegistry.Bookings> replyTo, Integer user_id) implements Command{}
    public final record DeleteAllUserBookings(ActorRef<ShowActor.Response> replyTo, Integer user_id) implements Command{}

    public final record DeleteAllBookings(ActorRef<WorkerActor.DeleteResponse> replyTo) implements Command{}

    public final record GetShowBookings(List<ShowActor.Booking> bookings) implements Command{}

    public final record Booking(Integer id,Integer user_id,Integer show_id , Integer seats_booked)implements CborSerializable{}
    public final record Bookings(List<ShowActor.Booking> bookings)implements CborSerializable{}
    public final record Showlist(List<ShowActor.Show> showlist)implements CborSerializable{}

    public final record Response(boolean flag) implements Command{}

    public final record DeleteResponse(boolean flag) implements Command{}



    public final record ShowByTheatre(Integer theatre_id,ActorRef<Showlist> replyTo) implements Command{}
    public final record TheatreOfShow(Boolean status,ShowActor.Show showDetails,Integer i,ActorRef<Showlist> replyTo) implements Command{}

    

    //private WorkerActor(ActorContext<Command> context, Map<Integer,ActorRef<ShowActor.Command>> showActors) {
    private WorkerActor(ActorContext<Command> context,Map<Integer,EntityRef<ShowActor.Command>> showActors) {
        super(context);
        this.numProcessed = 1;
        this.showActors = showActors;
        this.selfRef = getContext().getSelf(); 
    }

    public static Behavior<Command> create(Map<Integer,EntityRef<ShowActor.Command>> showActors) {
        return Behaviors.setup(context->new WorkerActor(context,showActors));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetShowBookings.class, this::onGetShowBookings)
                .onMessage(GetUserBookings.class, this::onGetUserBookings)
                .onMessage(ShowByTheatre.class,this::onShowByTheatre)
                .onMessage(TheatreOfShow.class,this::onTheatreOfShow)
                .onMessage(Response.class,this::onGetResponse)
                .onMessage(DeleteAllUserBookings.class, this::onDeleteAllUserBookings)
                .onMessage(DeleteAllBookings.class, this::onDeleteAllBookings)
                .onMessage(DeleteResponse.class, this::onGetDeleteResponse)
                .build();
    }

    private Behavior<Command> onGetShowBookings(GetShowBookings message) {
        incrementProcessedAndAddBookings(message);
        if (this.numProcessed == 21) {
            sendUserBookings();
            return this;
        } else {
            requestMoreBookings();
        }
        return this;
    }
    
    private void incrementProcessedAndAddBookings(GetShowBookings message) {
        this.numProcessed += 1;
        for (ShowActor.Booking b : message.bookings) {
            this.userBookings.add(new BookingRegistry.Booking(b.id(), b.user_id(), b.show_id(), b.seats_booked()));
        }
    }
    
    private void sendUserBookings() {
        List<BookingRegistry.Booking> result = new ArrayList<>();
        for (BookingRegistry.Booking booking : this.userBookings) {
            if (booking.user_id() == this.user_id) {
                result.add(booking);
            }
        }
        this.replyTo.tell(new BookingRegistry.Bookings(Collections.unmodifiableList(new ArrayList<>(result))));
    }
    
    private void requestMoreBookings() {
        showActors.get(numProcessed).tell(new ShowActor.GetBookings(this.selfRef));
    }

    private Behavior<Command> onGetUserBookings(GetUserBookings message) {
        this.replyTo = message.replyTo;
        this.user_id = message.user_id;
        showActors.get(1).tell(new ShowActor.GetBookings(this.selfRef));
        return this;
    }


    private Behavior<Command> onTheatreOfShow(TheatreOfShow message) {
        if(message.status)
            this.showOfTheatre.add(message.showDetails);
        if(message.i == 0){
            message.replyTo().tell(new Showlist(showOfTheatre));
            return this;
        }
        return this;
    }

    private Behavior<Command> onShowByTheatre(ShowByTheatre message) {
        int i=20;
        for (int k=1;k<=20;k++) {
            showActors.get(1).tell(new ShowActor.GetTheatre(this.selfRef, --i, message.theatre_id,message.replyTo));
        }
        return this;
    }


    private Behavior<Command> onGetResponse(Response message) {
        this.numProcessed += 1;
        if(message.flag())
            this.flag = true;

        if(this.numProcessed == 21){
            this.replyToDelete.tell(new ShowActor.Response(this.flag));
            this.numProcessed = 1;
            this.flag=  false;
            return this;
        }
        return this;
    }

    private Behavior<Command> onDeleteAllUserBookings(DeleteAllUserBookings message) {
        this.replyToDelete = message.replyTo;
        showActors.get(1).tell(new ShowActor.DeleteUserBooking(message.user_id, 1, null, this.selfRef));
        return this;
    }


    private Behavior<Command> onGetDeleteResponse(DeleteResponse message) {
        incrementProcessedAndSetFlag(message);
        if (this.numProcessed == 21) {
            resetAndReply();
            return this;
        } else {
            requestMoreDeletions();
        }
        return this;
    }
    
    private void incrementProcessedAndSetFlag(DeleteResponse message) {
        this.numProcessed += 1;
        if (message.flag()) {
            this.flag = true;
        }
    }
    
    private void resetAndReply() {
        this.replyToDeleteAll.tell(new DeleteResponse(this.flag));
        this.numProcessed = 1;
        this.flag = false;
    }
    
    private void requestMoreDeletions() {
        EntityRef<ShowActor.Command> ref = showActors.get(numProcessed);
        ref.tell(new ShowActor.DeleteAllBookings(this.selfRef));
    }

    private Behavior<Command> onDeleteAllBookings(DeleteAllBookings message) {
        this.replyToDeleteAll = message.replyTo;
        EntityRef<ShowActor.Command> ref = showActors.get(1);
        ref.tell(new ShowActor.DeleteAllBookings(this.selfRef));
        return this;
    }

}