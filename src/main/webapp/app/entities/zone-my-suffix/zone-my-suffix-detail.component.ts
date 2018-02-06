import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ZoneMySuffix } from './zone-my-suffix.model';
import { ZoneMySuffixService } from './zone-my-suffix.service';

@Component({
    selector: 'jhi-zone-my-suffix-detail',
    templateUrl: './zone-my-suffix-detail.component.html'
})
export class ZoneMySuffixDetailComponent implements OnInit, OnDestroy {

    zone: ZoneMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private zoneService: ZoneMySuffixService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInZones();
    }

    load(id) {
        this.zoneService.find(id)
            .subscribe((zoneResponse: HttpResponse<ZoneMySuffix>) => {
                this.zone = zoneResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInZones() {
        this.eventSubscriber = this.eventManager.subscribe(
            'zoneListModification',
            (response) => this.load(this.zone.id)
        );
    }
}
