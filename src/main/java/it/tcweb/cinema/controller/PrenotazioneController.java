package it.tcweb.cinema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tcweb.cinema.models.Prenotazione;
import it.tcweb.cinema.service.PrenotazioneService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/prenotazioni")
@Tag(name = "Prenotazioni", description = "Gestione delle prenotazioni del cinema")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService service;

    @GetMapping
    @Operation(summary = "Recupera tutte le prenotazioni", description = "Restituisce tutte le prenotazione presenti nel database db_cinema")
    public ResponseEntity<List<Prenotazione>> trovaTutti() {
        return ResponseEntity.ok(service.trovaTutti());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recupera una prenotazione tramite l'ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prenotazione trovata"),
            @ApiResponse(responseCode = "404", description = "Prenotazione non trovata"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<Prenotazione> trovaPerId(@PathVariable Integer id) {
        Prenotazione prenotazione = service.trovaPerId(id);

        if (prenotazione == null) {
            return ResponseEntity.notFound().build(); // Se non esiste ritorna 404
        }

        return ResponseEntity.ok(prenotazione); // Altrimenti 200 + dati
    }

    @PostMapping
    @Operation(summary = "Crea un prenotazione")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prenotazione creata"),
            @ApiResponse(responseCode = "400", description = "Prenotazione non creata per dati sbagliati"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<Prenotazione> crea(@Valid @RequestBody Prenotazione prenotazione,
            @RequestParam Integer idSpettacolo) {

        Prenotazione prenotazioneSalvato = service.salva(prenotazione, idSpettacolo);

        return ResponseEntity.status(201).body(prenotazioneSalvato);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Sostituisce la prenotazione con ID specificato")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prenotazione sostutuita"),
            @ApiResponse(responseCode = "400", description = "Prenotazione non sostutuita per dati sbagliati"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<Prenotazione> aggiorna(@PathVariable Integer id, @RequestBody Prenotazione prenotazione,
            @RequestParam Integer idSpettacolo) {
        if (service.trovaPerId(id) == null)
            return ResponseEntity.notFound().build();

        prenotazione.setId(id);

        return ResponseEntity.ok(service.salva(prenotazione, idSpettacolo));

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancella la prenotazione con ID specificato")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Prenotazione cancellata"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<Void> elimina(@PathVariable Integer id) {
        if (service.trovaPerId(id) == null)
            return ResponseEntity.notFound().build();

        service.elimina(id);
        return ResponseEntity.noContent().build();
    }
}
