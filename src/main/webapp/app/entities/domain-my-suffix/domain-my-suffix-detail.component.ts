import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { DomainMySuffix } from './domain-my-suffix.model';
import { DomainMySuffixService } from './domain-my-suffix.service';

@Component({
    selector: 'jhi-domain-my-suffix-detail',
    templateUrl: './domain-my-suffix-detail.component.html'
})
export class DomainMySuffixDetailComponent implements OnInit, OnDestroy {

    domain: DomainMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private domainService: DomainMySuffixService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDomains();
    }

    load(id) {
        this.domainService.find(id).subscribe((domain) => {
            this.domain = domain;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDomains() {
        this.eventSubscriber = this.eventManager.subscribe(
            'domainListModification',
            (response) => this.load(this.domain.id)
        );
    }
}
