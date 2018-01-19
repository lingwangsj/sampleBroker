import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ZoneMySuffix } from './zone-my-suffix.model';
import { ZoneMySuffixPopupService } from './zone-my-suffix-popup.service';
import { ZoneMySuffixService } from './zone-my-suffix.service';

@Component({
    selector: 'jhi-zone-my-suffix-delete-dialog',
    templateUrl: './zone-my-suffix-delete-dialog.component.html'
})
export class ZoneMySuffixDeleteDialogComponent {

    zone: ZoneMySuffix;

    constructor(
        private zoneService: ZoneMySuffixService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.zoneService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'zoneListModification',
                content: 'Deleted an zone'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-zone-my-suffix-delete-popup',
    template: ''
})
export class ZoneMySuffixDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private zonePopupService: ZoneMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.zonePopupService
                .open(ZoneMySuffixDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
