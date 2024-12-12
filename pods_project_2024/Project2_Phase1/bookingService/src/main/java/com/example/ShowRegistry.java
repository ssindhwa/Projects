package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import akka.http.javadsl.Http;

import java.util.*;
import java.io.*;

public class ShowRegistry extends AbstractBehavior<ShowRegistry.Command> {

    // actor protocol
    sealed interface Command {
    }

    final Http http = Http.get(getContext().getSystem());

    // Instance variables
    public Integer id;
    public Integer theatre_id;
    String title;
    public Integer price;
    public Integer seats_available;
    public static final List<ShowRegistry.Booking> bookings = new ArrayList<>();

    public final static record GetShow(Integer show_id, ActorRef<Show> replyTo) implements Command {
    }

    public final static record GetShowOfTheatre(Integer show_id, Integer theatre_id, ActorRef<Show> replyTo)
            implements Command {
    }

    public final static record Show(Integer id, Integer theatre_id, String title, Integer price,
            Integer seats_available) {
    }

    public final static record Booking(Integer id, Integer user_id, Integer show_id, Integer seats_booked) {
    }

    public final static record Response(String description) {
    }

    public final static record UserBookings(List<ShowRegistry.Booking> bookings) {

    }

    public final static record AddBooking(Booking booking, ActorRef<ShowRegistry.Booking> replyTo) implements Command {
    }

    public final static record DeleteUserBooking(Integer user_id, Integer show_id, ActorRef<Response> replyTo)
            implements Command {
    }

    public final record GetAllUserBookings(Integer show_id, Integer user_id,
            ActorRef<ShowRegistry.UserBookings> replyTo)
            implements Command {
    }

    public final record DeleteAllUserBookings(Integer show_id, Integer user_id,
            ActorRef<ShowRegistry.Response> replyTo)
            implements Command {
    }

    public final record DeleteAllBookings(ActorRef<ShowRegistry.Response> replyTo)
            implements Command {
    }

    private ShowRegistry(ActorContext<Command> context, Integer id, Integer theatre_id, String title, Integer price,
            Integer seats_available) {
        super(context);
        this.id = id;
        this.theatre_id = theatre_id;
        this.title = title;
        this.price = price;
        this.seats_available = seats_available;
    }

    public static Behavior<Command> create(Integer id, Integer theatre_id, String title, Integer price,
            Integer seats_available) {
        return Behaviors.setup(context -> new ShowRegistry(context, id, theatre_id, title, price, seats_available));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetShow.class, this::onGetShow)
                .onMessage(GetShowOfTheatre.class, this::onGetShowOfTheatre)
                .onMessage(AddBooking.class, this::onAddBooking)
                .onMessage(DeleteUserBooking.class, this::onDeleteUserBooking)
                .onMessage(GetAllUserBookings.class, this::onGetAllUserBookings)
                .onMessage(DeleteAllUserBookings.class, this::onDeleteAllUserBookings)
                .onMessage(DeleteAllBookings.class, this::onDeleteAllBookings)
                .build();
    }

    private Behavior<Command> onGetShow(GetShow command) {
        if (this.id.equals(command.show_id())) {
            command.replyTo().tell(new Show(this.id, this.theatre_id, this.title, this.price, this.seats_available));

        }
        return this;
    }

    private Behavior<Command> onGetShowOfTheatre(GetShowOfTheatre command) {
        if (this.theatre_id.equals(command.theatre_id())) {
            command.replyTo().tell(new Show(this.id, this.theatre_id, this.title, this.price, this.seats_available));
        } else {
            command.replyTo().tell(new Show(null, this.theatre_id, this.title, this.price, this.seats_available));
        }
        return this;
    }

    private Behavior<Command> onGetAllUserBookings(GetAllUserBookings command) {
        ListIterator<Booking> iter = bookings.listIterator();
        List<Booking> userBookings = new ArrayList<>();
        while (iter.hasNext()) {
            Booking currentBooking = iter.next();
            if (Objects.equals(currentBooking.user_id, command.user_id)
                    && Objects.equals(currentBooking.show_id, command.show_id)) {
                userBookings.add(currentBooking);
            }
        }
        command.replyTo().tell(new UserBookings(userBookings));
        return this;
    }

    private Behavior<Command> onDeleteAllUserBookings(DeleteAllUserBookings command) {
        ListIterator<Booking> iter = bookings.listIterator();
        boolean hasBookings = false;
        while (iter.hasNext()) {
            Booking currentBooking = iter.next();
            if (Objects.equals(currentBooking.user_id, command.user_id)
                    && Objects.equals(currentBooking.show_id, command.show_id)) {
                hasBookings = true;

                // Amount to make these bookin to be returned to users wallets
                String walletRefundStatus = WalletService.refund(command.user_id,
                        currentBooking.seats_booked * this.price, http);
                System.out.println("walletRefundStatus - " + walletRefundStatus);

                this.seats_available += currentBooking.seats_booked;
                iter.remove();
            }
        }
        if (hasBookings) {
            command.replyTo().tell(new Response("Done"));
        } else {
            command.replyTo().tell(new Response("NOT_FOUND"));
        }
        return this;
    }

    private Behavior<Command> onDeleteAllBookings(DeleteAllBookings command) {
        Map<Integer, Integer> totalRefunds = new HashMap<>();

        ListIterator<Booking> iter = bookings.listIterator();
        while (iter.hasNext()) {
            Booking currentBooking = iter.next();
            if (totalRefunds.containsKey(currentBooking.user_id)) {
                totalRefunds.put(currentBooking.user_id,
                        totalRefunds.get(currentBooking.user_id)
                                + currentBooking.seats_booked * BookingRegistry.showPrices.get(currentBooking.show_id));
            } else {
                totalRefunds.put(currentBooking.user_id,
                        currentBooking.seats_booked * BookingRegistry.showPrices.get(currentBooking.show_id));
            }

            this.seats_available += currentBooking.seats_booked;
            iter.remove();
        }

        for (int user_id : totalRefunds.keySet()) {
            // deduct the total refund amount from the user's wallet
            String walletRefundStatus = WalletService.refund(user_id, totalRefunds.get(user_id), http);
        }
        command.replyTo().tell(new Response("Done"));
        return this;
    }

    private Behavior<Command> onAddBooking(AddBooking command) {
        Integer id = command.booking.id;
        Integer user_id = command.booking.user_id;
        Integer show_id = command.booking.show_id;
        Integer seats_booked = command.booking.seats_booked;

        // not sufficient seats available
        if (this.seats_available < seats_booked) {
            command.replyTo.tell(new ShowRegistry.Booking(null, null, null, null));
        } else {
            //check user should exist
            if (!ServiceUser.doesUserExist(user_id, http)) {
                command.replyTo.tell(new ShowRegistry.Booking(null, null, null, null));
            } else {
                String walletReductionStatus = WalletService.payment(user_id, seats_booked * this.price, http);
                System.out.println("walletReductionStatus - " + walletReductionStatus);

                if (walletReductionStatus == "FAIL") {
                    command.replyTo.tell(new ShowRegistry.Booking(null, null, null, null));
                } else {

                    ListIterator<Booking> iter = bookings.listIterator();
                    Booking newBooking = new Booking(id, user_id, show_id, seats_booked);
                    while (iter.hasNext()) {
                        Booking currentBooking = iter.next();
                        if (Objects.equals(currentBooking.show_id, show_id)
                                && Objects.equals(currentBooking.user_id, user_id)) {
                            // seats_available += currentBooking.seats_booked;
                            newBooking = new Booking(currentBooking.id, user_id, show_id,
                                    currentBooking.seats_booked + seats_booked);
                            iter.remove();
                        }
                    }

                    this.seats_available = this.seats_available - seats_booked;
                    bookings.add(newBooking);
                    command.replyTo().tell(newBooking);
                }
            }
        }
        return this;
    }

    private Behavior<Command> onDeleteUserBooking(DeleteUserBooking command) {
        ListIterator<Booking> iter = bookings.listIterator();
        boolean hasBookings = false;
        while (iter.hasNext()) {
            Booking currentBooking = iter.next();
            if (Objects.equals(currentBooking.show_id, command.show_id)
                    && Objects.equals(currentBooking.user_id, command.user_id)) {
                hasBookings = true;

                // debit
                String walletRefundStatus = WalletService.refund(command.user_id,
                        currentBooking.seats_booked * this.price, http);
                System.out.println("walletRefundStatus - " + walletRefundStatus);

                seats_available += currentBooking.seats_booked;
                iter.remove();
            }
        }
        if (hasBookings) {
            command.replyTo().tell(new Response("Done"));
        } else {
            command.replyTo().tell(new Response("Not_Found"));
        }
        return this;
    }

}