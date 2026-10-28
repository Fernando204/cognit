package com.example.cognit_invest.models.finance;

import com.example.cognit_invest.enums.DayOfWeek;
import com.example.cognit_invest.enums.Month;
import com.example.cognit_invest.enums.finance.FixedMovimentationPeriod;
import com.example.cognit_invest.enums.finance.MovimentationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

/// as movimentações fixas são para a programação financeira do usuário
/// ao definir uma movimentação fixa, o usuário define um periodo em que
/// essa movimentação que pode ser uma despesa ou uma entrada, vai ser criada no sistema
/// sempre que criar terá o status pendente, para que o usuário vá no sisstema e confirme ou cancele a movimentação
@Document(collection = "fixed_movimentation")
public class FixedMovimentation {
    @Id
    private String id;

    //id do usuário para identificação
    private String userId;

    //detalhes da movimentação
    private String title;
    private String description;
    private String category;

    //informações financeiras sobre a movimentação
    private BigDecimal value;
    private String accountId;//id da conta bancaria
    private MovimentationType type;//tipo da movimentação, se é entrada ou saida

    //periodo em que a movimentação vai ser criada podendo ser anual, mensal, semanal ou diario
    private FixedMovimentationPeriod period;

    ///
    /// para determinar o periodo em que a movimentação será feita
    /// se for anual usaremos o mês e o dia do mês, e o dia da semana será null
    /// se for mensal apenas o dia do mês e o resto será null
    /// se for semanal apenas o dia da semana
    /// se for diario as três variavéis abaixo serão nulas
    ///
    private Month month;
    private Integer dayOfMonth;
    private DayOfWeek dayOfWeek;

    private Instant lastCreation;
    private Instant nextCreation;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public FixedMovimentation(
            String accountId,
            String category,
            Integer dayOfMonth,
            DayOfWeek dayOfWeek,
            String description,
            Month month,
            FixedMovimentationPeriod period,
            String title,
            MovimentationType type,
            String userId,
            BigDecimal value,
            Instant nextCreation
    ){
        this.accountId = accountId;
        this.category = category;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.description = description;
        this.month = month;
        this.period = period;
        this.title = title;
        this.type = type;
        this.userId = userId;
        this.value = value;
        this.nextCreation = nextCreation;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getLastCreation() {
        return lastCreation;
    }

    public void setLastCreation(Instant lastCreation) {
        this.lastCreation = lastCreation;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Instant getNextCreation() {
        return nextCreation;
    }

    public void setNextCreation(Instant nextCreation) {
        this.nextCreation = nextCreation;
    }

    public FixedMovimentationPeriod getPeriod() {
        return period;
    }

    public void setPeriod(FixedMovimentationPeriod period) {
        this.period = period;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovimentationType getType() {
        return type;
    }

    public void setType(MovimentationType type) {
        this.type = type;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
