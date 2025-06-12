

CREATE TABLE report_requests (
    id BINARY(16) NOT NULL PRIMARY KEY,


    status VARCHAR(20) NOT NULL,


    report_type VARCHAR(100) NOT NULL,

    parameters TEXT,


    requested_at DATETIME(6) NOT NULL,

    report_start_date DATE,

    report_end_date DATE,


    completed_at DATETIME(6)
);


CREATE INDEX idx_report_requests_status ON report_requests (status);