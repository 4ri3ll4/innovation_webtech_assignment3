ALTER TABLE event
    ADD CONSTRAINT chk_event_date_not_past CHECK (event_date >= CURRENT_DATE);

ALTER TABLE ticket
    ADD CONSTRAINT chk_ticket_price_positive CHECK (price > 0),
    ADD CONSTRAINT chk_ticket_quantity_non_negative CHECK (quantity_available >= 0);

