import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DomainMySuffix } from './domain-my-suffix.model';
import { DomainMySuffixPopupService } from './domain-my-suffix-popup.service';
import { DomainMySuffixService } from './domain-my-suffix.service';

@Component({
    selector: 'jhi-domain-my-suffix-delete-dialog',
    templateUrl: './domain-my-suffix-delete-dialog.component.html'
})
export class DomainMySuffixDeleteDialogComponent {

    domain: DomainMySuffix;

    constructor(
        private domainService: DomainMySuffixService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.domainService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'domainListModification',
                content: 'Deleted an domain'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-domain-my-suffix-delete-popup',
    template: ''
})
export class DomainMySuffixDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private domainPopupService: DomainMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.domainPopupService
                .open(DomainMySuffixDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
