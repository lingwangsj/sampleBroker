import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DomainMySuffix } from './domain-my-suffix.model';
import { DomainMySuffixPopupService } from './domain-my-suffix-popup.service';
import { DomainMySuffixService } from './domain-my-suffix.service';

@Component({
    selector: 'jhi-domain-my-suffix-dialog',
    templateUrl: './domain-my-suffix-dialog.component.html'
})
export class DomainMySuffixDialogComponent implements OnInit {

    domain: DomainMySuffix;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private domainService: DomainMySuffixService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.domain.id !== undefined) {
            this.subscribeToSaveResponse(
                this.domainService.update(this.domain));
        } else {
            this.subscribeToSaveResponse(
                this.domainService.create(this.domain));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DomainMySuffix>>) {
        result.subscribe((res: HttpResponse<DomainMySuffix>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: DomainMySuffix) {
        this.eventManager.broadcast({ name: 'domainListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-domain-my-suffix-popup',
    template: ''
})
export class DomainMySuffixPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private domainPopupService: DomainMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.domainPopupService
                    .open(DomainMySuffixDialogComponent as Component, params['id']);
            } else {
                this.domainPopupService
                    .open(DomainMySuffixDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
