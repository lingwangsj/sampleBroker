import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { APGroupMySuffix } from './ap-group-my-suffix.model';
import { APGroupMySuffixPopupService } from './ap-group-my-suffix-popup.service';
import { APGroupMySuffixService } from './ap-group-my-suffix.service';

@Component({
    selector: 'jhi-ap-group-my-suffix-delete-dialog',
    templateUrl: './ap-group-my-suffix-delete-dialog.component.html'
})
export class APGroupMySuffixDeleteDialogComponent {

    aPGroup: APGroupMySuffix;

    constructor(
        private aPGroupService: APGroupMySuffixService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.aPGroupService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'aPGroupListModification',
                content: 'Deleted an aPGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ap-group-my-suffix-delete-popup',
    template: ''
})
export class APGroupMySuffixDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private aPGroupPopupService: APGroupMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.aPGroupPopupService
                .open(APGroupMySuffixDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
