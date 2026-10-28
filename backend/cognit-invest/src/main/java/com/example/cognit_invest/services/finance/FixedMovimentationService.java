package com.example.cognit_invest.services.finance;


import com.example.cognit_invest.DTOs.finance.fixedMovimentation.FixedMovimentationRequestDTO;
import com.example.cognit_invest.DTOs.finance.fixedMovimentation.FixedMovimentationResponseDTO;
import com.example.cognit_invest.enums.DayOfWeek;
import com.example.cognit_invest.enums.Month;
import com.example.cognit_invest.enums.finance.FixedMovimentationPeriod;
import com.example.cognit_invest.models.finance.FixedMovimentation;
import com.example.cognit_invest.repositories.finance.FixedMovimentationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;

@Service
public class FixedMovimentationService {

    private static final Logger log = LoggerFactory.getLogger(FixedMovimentationService.class);
    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    private final FixedMovimentationRepository fixedMovimentationRepository;
    private final MovimentationService movimentationService;

    public FixedMovimentationService(
            MovimentationService movimentationService,
            FixedMovimentationRepository fixedMovimentationRepository
    ) {
        this.movimentationService = movimentationService;
        this.fixedMovimentationRepository = fixedMovimentationRepository;
    }

    // ======================= CRUD =======================

    public FixedMovimentationResponseDTO create(String userId, FixedMovimentationRequestDTO req) {
        validateSchedule(req);

        FixedMovimentationPeriod period = req.period();
        Month month = period == FixedMovimentationPeriod.ANUAL ? req.month() : null;
        Integer dayOfMonth = usesDayOfMonth(period) ? req.dayOfMonth() : null;
        DayOfWeek dayOfWeek = period == FixedMovimentationPeriod.SEMANAL ? req.dayOfWeek() : null;

        LocalDate today = LocalDate.now(ZONE);
        Instant nextCreation = toInstant(calculateNext(period, month, dayOfMonth, dayOfWeek, today, true));

        FixedMovimentation fm = new FixedMovimentation(
                req.accountId(),
                req.category(),
                dayOfMonth,
                dayOfWeek,
                req.description(),
                month,
                period,
                req.title(),
                req.type(),
                userId,
                req.value(),
                nextCreation
        );
        Instant now = Instant.now();
        fm.setCreatedAt(now);
        fm.setUpdatedAt(now);

        return FixedMovimentationResponseDTO.from(fixedMovimentationRepository.save(fm));
    }

    public List<FixedMovimentationResponseDTO> findAll(String userId) {
        return fixedMovimentationRepository.findByUserIdAndDeletedAtIsNull(userId)
                .stream()
                .map(FixedMovimentationResponseDTO::from)
                .toList();
    }

    public FixedMovimentationResponseDTO findById(String userId, String id) {
        return FixedMovimentationResponseDTO.from(findOwned(userId, id));
    }

    public FixedMovimentationResponseDTO update(String userId, String id, FixedMovimentationRequestDTO req) {
        validateSchedule(req);
        FixedMovimentation fm = findOwned(userId, id);

        FixedMovimentationPeriod period = req.period();
        Month month = period == FixedMovimentationPeriod.ANUAL ? req.month() : null;
        Integer dayOfMonth = usesDayOfMonth(period) ? req.dayOfMonth() : null;
        DayOfWeek dayOfWeek = period == FixedMovimentationPeriod.SEMANAL ? req.dayOfWeek() : null;

        boolean scheduleChanged = fm.getPeriod() != period
                || fm.getMonth() != month
                || !Objects.equals(fm.getDayOfMonth(), dayOfMonth)
                || fm.getDayOfWeek() != dayOfWeek;

        fm.setTitle(req.title());
        fm.setDescription(req.description());
        fm.setCategory(req.category());
        fm.setValue(req.value());
        fm.setAccountId(req.accountId());
        fm.setType(req.type());
        fm.setPeriod(period);
        fm.setMonth(month);
        fm.setDayOfMonth(dayOfMonth);
        fm.setDayOfWeek(dayOfWeek);

        // só recalcula se a programação mudou, para não regerar no mesmo dia ao editar só o título
        if (scheduleChanged) {
            LocalDate today = LocalDate.now(ZONE);
            fm.setNextCreation(toInstant(calculateNext(period, month, dayOfMonth, dayOfWeek, today, true)));
        }
        fm.setUpdatedAt(Instant.now());

        return FixedMovimentationResponseDTO.from(fixedMovimentationRepository.save(fm));
    }

    // soft delete
    public void delete(String userId, String id) {
        FixedMovimentation fm = findOwned(userId, id);
        Instant now = Instant.now();
        fm.setDeletedAt(now);
        fm.setUpdatedAt(now);
        fixedMovimentationRepository.save(fm);
    }

    // ======================= Geração automática =======================

    @Scheduled(cron = "0 48 21 * * *", zone = "America/Sao_Paulo")
    public void run() {
        com.example.cognit_invest.services.internal.Logger.info("Iniciando verificação e criação de movimentações programadas");
        Instant endOfToday = toInstant(LocalDate.now(ZONE).plusDays(1));

        fixedMovimentationRepository.findByDeletedAtIsNullAndNextCreationLessThan(endOfToday)
                .forEach(fm -> {
                    try {
                        checkAndCreate(fm);
                    } catch (Exception e) {
                        log.error("Falha ao processar movimentação fixa {}", fm.getId(), e);
                    }
                });
    }

    public void checkAndCreate(FixedMovimentation fm) {
        if (fm.getDeletedAt() != null) return;

        LocalDate today = LocalDate.now(ZONE);
        LocalDate due = fm.getNextCreation().atZone(ZONE).toLocalDate();

        // recupera dias perdidos: cria uma movimentação para cada data vencida
        while (!due.isAfter(today)) {
            // saveMovimentation deve usar fm.getNextCreation() como data de referência
            // e criar a movimentação com status PENDENTE
            movimentationService.saveMovimentation(fm);
            com.example.cognit_invest.services.internal.Logger.info("Criando movimentação automaticamente: "+fm.getTitle());
            fm.setLastCreation(Instant.now());
            fm.setNextCreation(toInstant(calculateNext(
                    fm.getPeriod(), fm.getMonth(), fm.getDayOfMonth(), fm.getDayOfWeek(), due, false)));
            fm.setUpdatedAt(Instant.now());
            // salva a cada iteração para não perder progresso se algo falhar no meio
            fixedMovimentationRepository.save(fm);

            due = fm.getNextCreation().atZone(ZONE).toLocalDate();
        }
    }

    // ======================= Helpers =======================

    private FixedMovimentation findOwned(String userId, String id) {
        return fixedMovimentationRepository.findByIdAndUserIdAndDeletedAtIsNull(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movimentação fixa não encontrada"));
    }

    private boolean usesDayOfMonth(FixedMovimentationPeriod period) {
        return period == FixedMovimentationPeriod.MENSAL || period == FixedMovimentationPeriod.ANUAL;
    }

    private void validateSchedule(FixedMovimentationRequestDTO req) {
        switch (req.period()) {
            case ANUAL -> {
                if (req.month() == null || req.dayOfMonth() == null) {
                    throw badRequest("Período anual exige mês e dia do mês");
                }
            }
            case MENSAL -> {
                if (req.dayOfMonth() == null) {
                    throw badRequest("Período mensal exige o dia do mês");
                }
            }
            case SEMANAL -> {
                if (req.dayOfWeek() == null) {
                    throw badRequest("Período semanal exige o dia da semana");
                }
            }
            case DIARIO -> { }
        }
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    private Instant toInstant(LocalDate date) {
        return date.atStartOfDay(ZONE).toInstant();
    }

    /**
     * Calcula a próxima data de criação a partir de "from".
     * inclusive = true: se "from" já é um dia válido, retorna "from" (usado ao criar/editar).
     * inclusive = false: retorna sempre uma data depois de "from" (usado após gerar).
     */
    private LocalDate calculateNext(
            FixedMovimentationPeriod period,
            Month month,
            Integer dayOfMonth,
            DayOfWeek dayOfWeek,
            LocalDate from,
            boolean inclusive
    ) {
        return switch (period) {
            case DIARIO -> inclusive ? from : from.plusDays(1);

            case SEMANAL -> {
                java.time.DayOfWeek dow = toJavaDayOfWeek(dayOfWeek);
                yield from.with(inclusive
                        ? TemporalAdjusters.nextOrSame(dow)
                        : TemporalAdjusters.next(dow));
            }

            case MENSAL -> {
                YearMonth ym = YearMonth.from(from);
                LocalDate candidate = atClamped(ym, dayOfMonth);
                if (isTooEarly(candidate, from, inclusive)) {
                    candidate = atClamped(ym.plusMonths(1), dayOfMonth);
                }
                yield candidate;
            }

            case ANUAL -> {
                YearMonth ym = YearMonth.of(from.getYear(), toJavaMonth(month));
                LocalDate candidate = atClamped(ym, dayOfMonth);
                if (isTooEarly(candidate, from, inclusive)) {
                    candidate = atClamped(ym.plusYears(1), dayOfMonth);
                }
                yield candidate;
            }
        };
    }

    private boolean isTooEarly(LocalDate candidate, LocalDate from, boolean inclusive) {
        return inclusive ? candidate.isBefore(from) : !candidate.isAfter(from);
    }

    // dia 31 em mês curto vira o último dia do mês
    private LocalDate atClamped(YearMonth ym, int dayOfMonth) {
        return ym.atDay(Math.min(dayOfMonth, ym.lengthOfMonth()));
    }

    private java.time.DayOfWeek toJavaDayOfWeek(DayOfWeek day) {
        return switch (day) {
            case SEGUNDA_FEIRA -> java.time.DayOfWeek.MONDAY;
            case TERÇA_FEIRA -> java.time.DayOfWeek.TUESDAY;
            case QUARTA_FEIRA -> java.time.DayOfWeek.WEDNESDAY;
            case QUINTA_FEIRA -> java.time.DayOfWeek.THURSDAY;
            case SEXTA_FEIRA -> java.time.DayOfWeek.FRIDAY;
            case SABADO -> java.time.DayOfWeek.SATURDAY;
            case DOMINGO -> java.time.DayOfWeek.SUNDAY;
        };
    }

    private java.time.Month toJavaMonth(Month month) {
        return switch (month) {
            case JANEIRO -> java.time.Month.JANUARY;
            case FEVEREIRO -> java.time.Month.FEBRUARY;
            case MARCO -> java.time.Month.MARCH;
            case ABRIL -> java.time.Month.APRIL;
            case MAIO -> java.time.Month.MAY;
            case JUNHO -> java.time.Month.JUNE;
            case JULHO -> java.time.Month.JULY;
            case AGOSTO -> java.time.Month.AUGUST;
            case SETEMBRO -> java.time.Month.SEPTEMBER;
            case OUTUBRO -> java.time.Month.OCTOBER;
            case NOVEMBRO -> java.time.Month.NOVEMBER;
            case DEZEMBRO -> java.time.Month.DECEMBER;
        };
    }
}