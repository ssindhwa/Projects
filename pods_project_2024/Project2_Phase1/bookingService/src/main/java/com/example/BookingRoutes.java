package com.example;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.*;

import com.example.BookingRegistry;
import com.example.ShowRegistry;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.marshallers.jackson.Jackson;

import static akka.http.javadsl.server.Directives.*;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// booking-routes-class
public class BookingRoutes {
  private final static Logger log = LoggerFactory.getLogger(BookingRoutes.class);
  private final ActorRef<BookingRegistry.Command> bookingRegistryActor;
  private final Duration askTimeout;
  private final Scheduler scheduler;

  public BookingRoutes(ActorSystem<?> system, ActorRef<BookingRegistry.Command> bookingRegistryActor) {
    this.bookingRegistryActor = bookingRegistryActor;
    scheduler = system.scheduler();
    askTimeout = system.settings().config().getDuration("my-app.routes.ask-timeout");
  }

  private CompletionStage<BookingRegistry.TheatresReply> getTheatres() {
    return AskPattern.ask(bookingRegistryActor, BookingRegistry.GetTheatres::new,
        askTimeout, scheduler);
  }

  private CompletionStage<ShowRegistry.Show> getShow(Integer show_id) {
    ActorRef<ShowRegistry.Command> showRegistryActor = BookingRegistry.showActors.get(show_id);
    return AskPattern.ask(showRegistryActor, ref -> new ShowRegistry.GetShow(show_id, ref), askTimeout, scheduler);
  }

  private CompletionStage<List<ShowRegistry.Show>> getShowsOfTheatre(Integer theatre_id) {
    List<CompletionStage<ShowRegistry.Show>> futures = new ArrayList<>();

    for (int show_id : BookingRegistry.showActors.keySet()) {
      ActorRef<ShowRegistry.Command> showRegistryActor = BookingRegistry.showActors.get(show_id);
      futures.add(
          AskPattern.ask(showRegistryActor, ref -> new ShowRegistry.GetShowOfTheatre(show_id, theatre_id, ref),
              askTimeout,
              scheduler));

    }

    CompletableFuture<ShowRegistry.Show>[] futuresArray = futures.toArray(new CompletableFuture[0]);
    CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futuresArray);

    return allOfFuture.thenApply(v -> {
      return futures.stream()
          .map(CompletionStage::toCompletableFuture)
          .map(CompletableFuture::join)
          .filter(show -> show != null && show.id() != null) // Filter out null shows and shows with null IDs
          .collect(Collectors.toList()); // Collect non-null shows into a list
    });
  }

  private CompletionStage<ShowRegistry.Booking> addBooking(BookingRegistry.Booking booking) {
    return AskPattern.ask(bookingRegistryActor, ref -> new BookingRegistry.AddBooking(booking, ref), askTimeout,
        scheduler);
  }

  private CompletionStage<ShowRegistry.Response> deleteUserBooking(Integer user_id, Integer show_id) {
    ActorRef<ShowRegistry.Command> showRegistryActor = BookingRegistry.showActors.get(show_id);
    return AskPattern.ask(showRegistryActor, ref -> new ShowRegistry.DeleteUserBooking(user_id, show_id, ref),
        askTimeout, scheduler);
  }

  private CompletionStage<List<ShowRegistry.UserBookings>> getAllUserBookings(Integer user_id) {
    List<CompletionStage<ShowRegistry.UserBookings>> futures = new ArrayList<>();
    for (int show_id : BookingRegistry.showActors.keySet()) {
      ActorRef<ShowRegistry.Command> showRegistryActor = BookingRegistry.showActors.get(show_id);
      futures.add(
          AskPattern.ask(showRegistryActor, ref -> new ShowRegistry.GetAllUserBookings(show_id, user_id, ref),
              askTimeout,
              scheduler));
    }

    CompletableFuture<ShowRegistry.UserBookings>[] futuresArray = futures.toArray(new CompletableFuture[0]);
    CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futuresArray);

    return allOfFuture.thenApply(v -> {
      return futures.stream()
          .map(CompletionStage::toCompletableFuture)
          .map(CompletableFuture::join)
          .filter(booking -> booking.bookings().size() != 0)
          .collect(Collectors.toList());
    });
  }

  private CompletionStage<List<ShowRegistry.Response>> deleteAllUserBookings(Integer user_id) {
    List<CompletionStage<ShowRegistry.Response>> futures = new ArrayList<>();
    for (int show_id : BookingRegistry.showActors.keySet()) {
      ActorRef<ShowRegistry.Command> showRegistryActor = BookingRegistry.showActors.get(show_id);
      futures.add(
          AskPattern.ask(showRegistryActor, ref -> new ShowRegistry.DeleteAllUserBookings(show_id, user_id, ref),
              askTimeout,
              scheduler));
    }

    CompletableFuture<ShowRegistry.Response>[] futuresArray = futures.toArray(new CompletableFuture[0]);
    CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futuresArray);

    return allOfFuture.thenApply(v -> {
      return futures.stream()
          .map(CompletionStage::toCompletableFuture)
          .map(CompletableFuture::join)
          .filter(response -> response.description() == "Done")
          .collect(Collectors.toList());
    });
  }

  private CompletionStage<ShowRegistry.Response> deleteAllBookings() {
    ActorRef<ShowRegistry.Command> showRegistryActor = BookingRegistry.showActors.get(1);
    return AskPattern.ask(showRegistryActor, ref -> new ShowRegistry.DeleteAllBookings(ref),
        askTimeout, scheduler);
  }

  public Route bookingRoutes() {
    return concat(
        // API-1
        pathPrefix("theatres",
            () -> pathEnd(() -> get(
                () -> onSuccess(getTheatres(), theatres -> complete(StatusCodes.OK, theatres.theatres(),
                    Jackson.marshaller()))))),
        // API-3
        pathPrefix("shows", () -> path(PathMatchers.segment(), (String show_id) -> get(() -> {
          if (Integer.parseInt(show_id) > 20 || Integer.parseInt(show_id) < 1) {
            return complete(StatusCodes.NOT_FOUND, "Show not found");
          }
          return onSuccess(getShow(Integer.parseInt(show_id)), showDetails -> {
            if (showDetails != null) {
              return complete(StatusCodes.OK, showDetails, Jackson.marshaller());
            } else {
              return complete(StatusCodes.NOT_FOUND, "Show not found");
            }
          });
        }))),
        // API-2
        path(PathMatchers.segment("shows").slash("theatres").slash(PathMatchers.segment()),
            (String theatre_id) -> get(() -> {
              return onSuccess(getShowsOfTheatre(Integer.parseInt(theatre_id)), showDetails -> {
                if (Integer.parseInt(theatre_id) > 10 || Integer.parseInt(theatre_id) < 1) {
                  return complete(StatusCodes.NOT_FOUND, "Theatre not found");
                }
                if (showDetails != null) {
                  return complete(StatusCodes.OK, showDetails, Jackson.marshaller());
                } else {
                  return complete(StatusCodes.NOT_FOUND, "Entity not found");
                }
              });
            })),
        // API-5
        pathPrefix("bookings",
            () -> pathEnd(() -> post(() -> entity(
                Jackson.unmarshaller(BookingRegistry.Booking.class),
                booking -> onSuccess(addBooking(booking), bookingDetails -> {
                
                  if (bookingDetails.id() != null) {
                    return complete(StatusCodes.OK,bookingDetails, Jackson.marshaller());
                  } else {
                    return complete(StatusCodes.BAD_REQUEST, "Entity not found");
                  }
                }))))),
        // API-7
        path(
            PathMatchers.segment("bookings").slash("users").slash(PathMatchers.segment()).slash("shows")
                .slash(PathMatchers.segment()),
            (String user_id, String show_id) -> delete(() -> {
              if (Integer.parseInt(show_id) > 20 || Integer.parseInt(show_id) < 1) {
                return complete(StatusCodes.NOT_FOUND, "Show not found");
              }
              return onSuccess(deleteUserBooking(Integer.parseInt(user_id), Integer.parseInt(show_id)), showDetails -> {
                if (showDetails.description() != "Not_Found") {
                  return complete(StatusCodes.OK);
                } else {
                  return complete(StatusCodes.NOT_FOUND, "Show not found");
                }
              });
            })),
        // API-4
        path(PathMatchers.segment("bookings").slash("users").slash(PathMatchers.segment()),
            (String user_id) -> get(() -> {
              return onSuccess(getAllUserBookings(Integer.parseInt(user_id)), bookingDetails -> {
                List<ShowRegistry.Booking> bookings = new ArrayList<>();
                for (ShowRegistry.UserBookings ub : bookingDetails) {
                  for (ShowRegistry.Booking b : ub.bookings()) {
                    bookings.add(b);
                  }
                }
                if (bookingDetails != null) {
                  return complete(StatusCodes.OK, bookings, Jackson.marshaller());
                } else {
                  return complete(StatusCodes.NOT_FOUND, "Bookings not found");
                }
              });
            })),
        // API-6
        path(PathMatchers.segment("bookings").slash("users").slash(PathMatchers.segment()),
            (String user_id) -> delete(() -> {
              return onSuccess(deleteAllUserBookings(Integer.parseInt(user_id)), bookingDetails -> {
                if (bookingDetails.size() != 0) {
                  return complete(StatusCodes.OK);
                } else {
                  return complete(StatusCodes.NOT_FOUND, "Entity not found");
                }
              });
            })),
        // API-8
        pathPrefix("bookings",
            () -> pathEnd(() -> delete(() -> {
              return onSuccess(deleteAllBookings(), response -> {
                return complete(StatusCodes.OK);
              });
            })))

    );
  }
}